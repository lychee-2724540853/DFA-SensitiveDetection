old = open("1.txt",'r',encoding='utf8')

newfile = open("temp.txt",'w',encoding='utf8')

for line in old:
    line = line.strip()
    line = line.replace('\'','')
    line = line.replace(':','')
    line = line.replace(' ',',')

    newfile.write(line+'\n')

old.close()
newfile.close()