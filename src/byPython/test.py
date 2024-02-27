import  time


def getTime(t):
    timeStamp = t/1000
    timeArray = time.localtime(timeStamp)
    hours = time.strftime("%H", timeArray)

    # 统计每天的小时段内的数据
    hoursList = ['01','02','03','04','05','06','07','12']
    if hours in hoursList:
        print("true")
        return True
    else:
        print("不在查询的范围内")
        return False
if __name__ == '__main__':
    getTime(1636702596660)