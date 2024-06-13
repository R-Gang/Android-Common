#!/bin/bash 
//定义合并文件方法
list_alldir(){
  #1.递归文件目录
  for file2 in $(ls -A "$1")
  do
  if [ -d "$1/$file2" ];then
  #echo "$1/$file2"
  list_alldir "$1/$file2"
  elif [ -f  "$1/$file2" ];then
      #2.如果后缀是.java，合并文件
      if [[ "$1/$file2" == *.java ]] ;then
      #echo "\n" >> out.txt
      #echo "$1/$file2" >> out.txt
      #echo "\n" >> out.txt
      cat "$1/$file2" >> out.txt
      fi
  fi
      done
}

# 这里指定为桌面了为指定的文件夹 从哪个文件夹提取
list_alldir C:\\WorkSpace\\YINSHI_CODE\\PoleClimb\\app\\src\\main\\java
