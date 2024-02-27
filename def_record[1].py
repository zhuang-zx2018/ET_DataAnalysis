# coding=utf-8
import os
import time

def record(txt_name,word):
    timespilt = time.time()
    localtime = time.localtime(timespilt)
    file_name = time.strftime('%Y.%m.%d',localtime)
    print(file_name)
    path = os.getcwd()+'\\'+file_name+'\\'+txt_name+'.txt'
    tf = open(path,'w')
    tf.write(word)
    tf.close()

if __name__ =='__main__':
    word = '''
    # coding=utf-8
    ***
    ==================================================
    引入：
    模块：
    总结：
    
    ==================================================

    ''' # 文本内容
    record('00test',word)

# +++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

# ------------------------------------------------------

# ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++