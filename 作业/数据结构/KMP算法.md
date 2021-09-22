## KMP

使用KMP算法是为了避免子串查找操作中出现的不

必要回溯。

匹配子串时，模式串和主串进行比较如果有一位字符不匹配时不需要进行回溯，一般分为两种情况。 

一是之前匹配的字符当中不含有最大重复项，此时从该位开始重新与模式串进行匹配不需要回溯。

![子串匹配1](.\子串匹配1.png)

二是之前匹配的字符当中含有最大重复项，模式串从开头的最大重复项后一位与目标串匹配

![子串匹配2](.\子串匹配2.png)

每次失配以后都不需要进行回溯，但是需要确认下一次模式串与目标串匹配开始的位置，这个位置可以通过next数组知道

### 求next数组

#### 基本原理

易知next[0] = -1，next[1] = 0

假设有模式串ababaaa

next[2]

<img src=".\1.png" alt="1" style="zoom:75%;" />

头尾都没有最大重复项，故next[2] = 0



next[3]

<img src=".\2.png" alt="2" style="zoom:75%;" />

头尾有最大重复项a和a，长度为1，故next[3] = 1

next[4]

<img src=".\3.png" alt="3" style="zoom:75%;" />

头尾有最大重复项ab和ab，长度为2，故next[5] = 2

next[5]

<img src=".\4.png" alt="4" style="zoom:75%;" />

头尾有最大重复项aba和aba，长度为3，故next[5] = 3

next[6]

<img src=".\5.png" alt="5" style="zoom:75%;" />

头尾有最大重复项a和a，长度为1，故next[6] = 1

#### 算法实现

```java
public int[] getNext(char[] chs){
    int[] next = new int[chs.length];
    int i = 0;
    int j = 1;
    next[0] = -1;
    next[1] = 0;
    while(j<chs.length - 1){
        if(chs[i] == chs[j]){
            //如果匹配，继续查找下一位是否也匹配
            next[j+1] = i + 1;
            j++;
            i++;
        }else if(i == 0){
            //不匹配，尾指针向后移一位
            next[j+1] == 0;
            j++;
        }else{
            //i = next[i]在这里的含义是，已经再找不到更大的最大匹配项了
            //将头指针往回退寻找较小的最大匹配项，直到最后i==0，尾指针向后移获取下一位的最大匹配项长度
            i = next[i];
        }
    }
    
    return next;
}
```

### kmp算法匹配子串

```java
public int indexOfSubString(char[] chs,char[] Tchs,int start,int[] next){
    int i = start;
    int j = 0;
    while(i<chs.length-1&&j<Tchs.length-1){
		if(j==-1||chs[i]==Tchs[j]){
            i++;
            j++;
        }else{
            j = next[j];
        }
    }
    if(j<Tchs.length){
        //不匹配
        return -1;
    }else{
        //匹配
		return i - Tchs.length;
    }
}
```

 