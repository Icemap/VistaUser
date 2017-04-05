package com.wqz.utils;

import java.util.List;

/**
 * Created by WangQiZhi on 2017/4/5.
 */

public class RandomUtils
{
    public static void randomTwoList(List<Integer> pano1List,List<Integer> pano2List)
    {
        for(int i = 0;i < pano1List.size();i++)
        {
            if(Math.random() >= 0.5)//50%
            {
                Integer swapIndex = (int) (Math.random() * pano1List.size());
                swap(pano1List,i,swapIndex);
                swap(pano2List,i,swapIndex);
            }
        }
    }

    private static void swap(List<Integer> list,Integer index1,Integer index2)
    {
        Integer temp;
        temp = list.get(index1);
        list.set(index1,list.get(index2));
        list.set(index2,temp);
    }
}
