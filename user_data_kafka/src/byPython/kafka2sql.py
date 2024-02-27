import traceback
from kafka import KafkaConsumer  # 导入第三方库
import time
import json
import uuid
from pymysql import NULL
from tqdm import tqdm
import sys

sys.path.append(r'C:\Users\zhuangzexuan\Documents\proj_python\user_data_kafka\src\byPython\config')

from dbUtils import DBUtil


def readTopic():
    #  与kafka建立连接
    consumer = KafkaConsumer('car_behavior', group_id='group_zzx', bootstrap_servers=['10.123.46.5:9092'],
                             api_version=(2, 0, 2))
    # consumer = KafkaConsumer('car_behavior', bootstrap_servers=['::1:9092'],api_version = (2,0,2))
    print("连接成功")

    index = 0   # 事件计数数
    count = 0   # 表格检查计数数
    for message in tqdm(consumer):  # 开始消费消息
        # if index >= 10:
        #     return
        # print ("%s %s value=%s" % (time.strftime('%Y-%m-%d %H:%M:%S', time.localtime(time.time())),message.topic,message.value))
        message_body = message.value.decode()
        # print("decode后的数据是 %s " % message_body)
        res = json.loads(message_body)  # 将json字符串转换为python字典形式
        appId = res["app_id"]  # app_ID TBOX上传的
        eventCode = res["event_code"]
        eventTime = int(res["event_time"])
        event_type = int(res['event_type'])

        if appId in exclodAppId():  # 筛选appID
            continue
        if eventCode == 'heart_beat':   # 筛选心跳测试包
            continue
        if getTime(eventTime) is False: # 筛选时间
            continue

        if event_type in [100, 103]:    # 筛选事件类型
            continue

        try:    # 每隔一段计数进行表格检查,如果表格不存在则创建表格
            if index >= 500000:
                count+=1
                count = check_Tb(count)
                index -= 500000
            Tbname = time2TbName(eventTime) # 转换时间名为表格名
            index += 1
            saveDb(res, Tbname) # 根据表格名写入数据库
            # print(index)
        except: # 输出错误信息
            index += 1
            print(traceback.format_exc())
            # sendEmail(traceback.format_exc())



# 将时间戳转换为表格名
def time2TbName(event_time):
    if event_time is NULL:
        return NULL
    else:
        timeStamp = int(event_time) / 1000
        Tbname = time.strftime("%m%d", time.localtime(timeStamp))
        return Tbname


# 每隔一段计时器数检查表格
def check_Tb(count):
    dbUtil = DBUtil()
    date = time.strftime("%m%d", time.localtime())
    check_sql = "SELECT * FROM car_behavior_info_{0} LIMIT 1;".format(date)
    res = DBUtil.get_one(dbUtil, check_sql)
    if res is not None:
        print("表格已检查{0}次".format(count))
        return count
    else:
        try:
            createDb(date)
            print("表格已创建！")
            return 0
        except:
            print('表格已存在!')
            return 0


# 将时间戳转换为可读时间格式
def time2str(event_time):
    if event_time is NULL:
        return NULL
    else:
        timeStamp = int(event_time) / 1000
        timeArray = time.localtime(timeStamp)
        otherStyleTime = time.strftime("%Y-%m-%d %H:%M:%S", timeArray)
        return otherStyleTime

# 输入表格名,根据表格名写入数据库
def saveDb(res, Tbname):
    dbUtil = DBUtil()
    pid = getUUId()
    currentTime = time.strftime("%Y-%m-%d %H:%M:%S", time.localtime())
    session_time = NULL
    system_result = NULL
    app_start = NULL

    event_time_2 = time2str(res["event_time"])  # 转换时间戳
    if res["event_value"] is not None:
        for jkey in res["event_value"]:
            if jkey == 'system_result':
                system_result = res["event_value"]['system_result']
                continue
            if jkey == 'app_start':
                app_start = res["event_value"]['app_start']
                continue
            if jkey == 'session_time':
                session_time = res["event_value"]['session_time']
                continue
    # --------------------------------------------------------------------------------------------------------------------
    # 注意为了运行效率，省去了format函数，在sql注入，生成表格，获取时间三个模块需要变更时间！！！
    # ---------------------------------------------------------------------------------------------------------------------

    # sql = "INSERT INTO douyin(`id`, `vin`,`type`,`time`,`create_time`) VALUES ('"+pid+"','123','抖音','"+currentTime+"', '"+currentTime+"')"
    sql = "INSERT INTO" + " `car_behavior_info_{0}`".format(Tbname) + " (`id`,`account_id`,`ai_in_car_id`,`app_id`,`app_name`,`app_version`,`car_brand`,`car_vin`,`device_brand`\
        ,`device_id`,`device_os_name`,`device_os_ver`,`event_code`,`event_time`,`event_time_2`,`event_type`,`hardware_ver`,`info_str`,`language`,`latitude`,`longitude`\
        ,`mcu_software_ver`,`net_type`,`platform`,`session_time`,`system_result`,`create_time`,`app_start`)\
         VALUES ('" + pid + "','" + res["account_id"] + "','" + res["ai_in_car_id"] + "','" + res["app_id"] + "','" + \
          res["app_name"] + "','" + res["app_version"] + "','" + res["car_brand"] + "','" + res["car_vin"] + "',\
        '" + res["device_brand"] + "','" + res["device_id"] + "','" + res["device_os_name"] + "','" + res[
              "device_os_ver"] + "','" + res["event_code"] + "','" + res["event_time"] + "','" + event_time_2 + "','" + \
          res["event_type"] + "','" + res["hardware_ver"] + "'\
        ,'" + res["info_str"] + "','" + res["language"] + "','" + res["latitude"] + "','" + res["longitude"] + "','" + \
          res["mcu_software_ver"] + "','" + res["net_type"] + "','" + res["platform"] + "','" + session_time + "'\
        ,'" + system_result + "','" + currentTime + "','" + app_start + "')"
    # print("save执行结果：" + str(DBUtil.save(dbUtil, sql)))
    DBUtil.save(dbUtil, sql)

# 根据日期名创建表格
def createDb(date):
    dbUtil = DBUtil()

    # today = time.strftime('%m%d', time.localtime())

    sql_Tb = """CREATE TABLE car_behavior_info_{0} (
    id               VARCHAR(50)       NOT NULL,
    account_id       VARCHAR(255)      DEFAULT '',
    ai_in_car_id     VARCHAR(255)      DEFAULT '',
    app_id           VARCHAR(255)      DEFAULT '',
    app_version      VARCHAR(255)      DEFAULT '',
    app_name         VARCHAR(255)      DEFAULT '',
    car_brand        VARCHAR(255)      DEFAULT '',
    car_vin          VARCHAR(255)      DEFAULT '',
    device_brand     VARCHAR(255)      DEFAULT '',
    device_id        VARCHAR(255)      DEFAULT '',
    device_os_name   VARCHAR(255)      DEFAULT '',
    device_os_ver    VARCHAR(255)      DEFAULT '',
    event_code       VARCHAR(255)      DEFAULT '',
    event_time       VARCHAR(255)      DEFAULT '',
    event_time_2     VARCHAR(255)      DEFAULT '',
    event_type       VARCHAR(255)      DEFAULT '',
    hardware_ver     VARCHAR(255)      DEFAULT '',
    info_str         VARCHAR(255)      DEFAULT '',
    language         VARCHAR(255)      DEFAULT '',
    latitude         VARCHAR(255)      DEFAULT '',
    longitude        VARCHAR(255)      DEFAULT '',
    mcu_software_ver VARCHAR(255)      DEFAULT '',
    net_type         VARCHAR(255)      DEFAULT '',
    platform         VARCHAR(255)      DEFAULT '',
    session_time     VARCHAR(255)      DEFAULT '',
    system_result    VARCHAR(255)      DEFAULT '',
    create_time      VARCHAR(255)      DEFAULT '',
    app_start        VARCHAR(255)      DEFAULT '',
    PRIMARY KEY (`id`),
    KEY (`create_time`));""".format(date)
    # TBname = 'car_behavior_info_' + tar_Date
    DBUtil.sql_creatTb(dbUtil, sql_Tb)
    print("{0}表格创建".format(date))
    return 0

# 设置UUID
def getUUId():
    uuidStr = str(uuid.uuid1())
    uuidArr = uuidStr.split("-")
    result = ""
    for i in uuidArr:
        result += i
    return result

# 除外的APP列表
def exclodAppId():
    appIdList = ['com.android.launcher', 'gaei.ecallbcall', 'android', 'com.gaei.vehichesetting', 'com.gaei.planet',
                 'com.gaei.gaeihvsmsettings',
                 'gaei.bluetooth', 'com.ts.app.newenergy', 'gaei.video', 'com.gaei.image', 'com.gaei.settings',
                 'com.android.systemui', 'com.trumpchi.assistant.app']
    return appIdList

# 日期筛选
def getTime(t):
    timeStamp = t / 1000
    timeArray = time.localtime(timeStamp)
    # hours = time.strftime("%H", timeArray)
    year = time.strftime("%Y", timeArray)

    # today = time.strftime("%d", time.localtime())
    # 统计每天的小时段内的数据
    # hoursList = ['01', '02', '03', '04', '05', '06', '07', '15']
    # 统计特定日期内的数据
    # daysList = ['15']

    if year == '2022':
        return True
    else:
        return False


if __name__ == '__main__':
    readTopic()
