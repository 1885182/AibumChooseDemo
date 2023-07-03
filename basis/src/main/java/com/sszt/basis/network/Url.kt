package com.sszt.basis.network

import com.sszt.basis.util.StringUtils


/**
 * <b>标题：</b> 统一设置请求地址 <br>
 * <b>描述：</b> <br>
 * <b>创建：</b>2021/11/12 8:56<br>
 * <b>更新：</b>时间： 更新人： 更新内容：
 * @param null
 * @return
 * @author mengwenyue
 * @version V1.0.0
 */
interface Url {
    companion object {

        val cla = Class.forName("com.sszt.cdslpetitionletter.BuildConfig")


        val urls =   cla.getField("BASE_URL")//反射获取app下build.gradle

        val url =   urls.get(cla).toString()//多渠道打包 正式打包用这个

        //const val url = "http://192.168.100.244:6004/" //

        const val agent = "sszt-cqjjqxf/"
        const val ws = "sszt-cqjjqxf/"

    }

}