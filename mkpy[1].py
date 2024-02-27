import time
import os
import sys
def mkpy(pyfile_name):

    timespilt = time.time()
    localtime = time.localtime(timespilt)
    file_name = time.strftime('%Y.%m.%d',localtime)
    print(file_name)
    path = os.getcwd()+'\\'+file_name+'\\'+pyfile_name+'.py'
    py = open(path,'w')
    py.close()

if __name__=='__main__':
    # pyname = sys.argv[1]
    mkpy("00move_images")

