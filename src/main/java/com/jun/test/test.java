package com.jun.test;

import org.junit.Test;

/**
 * @program: ssm-CRUD
 * @description:
 * @author: Jun
 * @create: 2022-03-07 13:04
 **/
public class test {

    @Test
    public void test1() {
        int[] arr = {1, 4, 6, 8, 2, 9, 24};
        for (int i = 0; i < arr.length - 1; i++) {
            for (int j = 0; j < arr.length - 1 - i; j++) {
                int temp = 0;
                if (arr[j] > arr[j + 1]) {
                    temp = arr[j];
                    arr[j] = arr[j + 1];
                    arr[j + 1] = temp;
                }
            }
        }
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}
