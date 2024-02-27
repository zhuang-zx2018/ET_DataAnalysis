import time
import os

def mkdir():
    timespilt = time.time()
    localtime = time.localtime(timespilt)
    file_name = time.strftime('%Y.%m.%d',localtime)
    print(file_name)
    os.mkdir(os.getcwd()+'\\'+file_name)

if __name__=='__main__':
    mkdir()

