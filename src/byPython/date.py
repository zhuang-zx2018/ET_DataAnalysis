import time
import datetime

# current_time = datetime.datetime.now()
# start_time = datetime.datetime(current_time.year, current_time.month, current_time.day, 8, 31, 0)
# print(start_time)
# end_time = datetime.datetime.now()
# print(end_time)
# diff_time_s = (end_time - start_time).seconds
# # diff_time_m = (end_time - start_time).minutes
# # diff_time_h = (end_time - start_time).hours
# print(diff_time_s)
# print(diff_time_m)
# print(diff_time_h)
# if diff_time_h == 3 and diff_time_m == 5:
#     print('end')

def getTime(t):
    timeStamp = t/1000
    timeArray = time.localtime(timeStamp)
    otherStyleTime = time.strftime("%Y-%m-%d %H:%M:%S", timeArray)
    print(otherStyleTime)
    return otherStyleTime

current_time = datetime.datetime.now()
print('current_time',current_time)
print('current_time.year',current_time.year)

if __name__ == '__main__':
    t = 1636679546293
    getTime(t)

print(getTime(t).year)