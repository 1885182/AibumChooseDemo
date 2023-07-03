package com.sszt.basis.dict

/**
 *
 * @param time 2021/7/26
 * @param des 字典表
 * @param author Meng
 *
 */
enum class DictEnum(val key: String) {
    //性别
    SEX("SEXSF"),

    //区域等级
    REGIONALLEVEL("XZQHJB"),

    //职务
    POST("ZHIWU"),

    //消息类别
    SMSTYPE("XXLB"),

    //部门类型
    BMTYPE("BMLX"),

    //政治面貌
    ZZMM("RY_ZZMM"),

    //婚姻状况
    HYZK("RY_HYZK"),

    //产品状态
    CPZT("CPZT"),

    //项目类别
    XMLB("XMLB"),

    //项目状态
    XMZT("XMZT"),

    //运维支撑业务
    YWZCYW("YWZCYW"),

    //行业分类
    HYFL("HYFL"),

    //岗位
    GW("GW"),

    //研发类型
    YFLX("YFLX"),

    //系统管理
    XTGL("XTGL"),

    //资源管理
    ZYGL("ZYGL"),

    //档案号配置字段
    DAHPZZD("DAHPZZD"),

    //保管期限
    STORAGEPERIOD("STORAGE_PERIOD"),

    //密级
    CLASSIFICATION("CLASSIFICATION"),

    //移交状态
    TRANSFER("TRANSFER"),

    //企业核准
    QYHZ("QYHZ"),

    //房屋状态
    HOUSESTATUS("HOUSE_STATUS"),

    //便民服务排序
    CONVENIENCESERVICESORT("CONVENIENCE_SERVICE_SORT"),

    //友情链接排序
    BLOGROLLSORT("BLOGROLL_SORT"),

    //房屋产权
    PROPERTYRIGHT("PROPERTYRIGHT"),

    //隐患类型
    DANGEROUSTYPE("DANGEROUS_TYPE"),

    //负责人证件类型
    FZRZJLX("FZRZJLX"),

    //宗教信仰
    ZJXY("ZJXY"),

    //特长
    RYGLTC("RYGL_TC"),

    //学历
    RYGLXL("RYGL_XL"),

    //级别
    RYGLJB("RYGL_JB"),

    //人口类型
    RKLX("RKLX"),

    //政治面貌
    RYZZMM("RY_ZZMM"),

    //职业类型
    ZYLX("ZYLX"),

    //与房东关系
    FWFDGX("FW_FDGX"),

    //部门层级
    BMCJ("BMCJ"),

    //人员类别
    RYLB("RYLB"),

    //建筑用途
    JZYT("JZYT"),

    //网格类型
    WGLX("WGLX"),

    //巡查事项
    MATTER("MATTER"),

    //部件类型
    ASSEMBLY("ASSEMBLY"),

    //执法类型
    ENFORCE("ENFORCE"),

    //事件规模
    SJGM("SJGM"),

    //事件来源
    SJLY("SJLY"),

    //绩效考核类型
    JXKH("JXKH"),

    //绩效考核评级类型
    KHPJ("KHPJ"),

    //户别
    HB("HB"),

    //执法分级
    GRADING("GRADING"),

    //院落类型
    YLLX("YLLX"),

    //生育类型
    SYLX("SYLX"),

    //节育类型
    JYLX("JYLX"),

    //优抚对象
    YFDX("YFDX"),

    //弱势群体
    RSQT("RSQT"),

    //社保办理保险
    SBBLBX("SBBLBX"),

    //低保类型
    DBLX("DBLX"),

    //残疾程度
    CJCD("CJCD"),

    //残疾类型
    CJLX("CJLX"),

    //主要生活来源
    ZYSHLY("ZYSHLY"),

    //养老保险情况
    YLBXQK("YLBXQK"),

    //医疗保险情况
    YLBX("YLBX"),

    //社区服务类型
    SQFWLX("SQFWLX"),

    //外出原因
    WCYY("WCYY"),

    //流动原因
    LDYY("LDYY"),

    //办证类型
    BZLX("BZLX"),

    //住所类型
    ZSLX("ZSLX"),

    //原罪名
    YZM("YZM"),

    //危险评估类型
    WXPGLX("WXPGLX"),

    //XJQK
    XJQK("XJQK"),

    //安置情况
    AZQK("AZQK"),

    //矫正类别
    JZLB("JZLB"),

    //接收方式
    JSFS("JSFS"),

    //四史情况
    SSQK("SSQK"),

    //三涉情况
    SANSQK("SANSQK"),

    //矫正小组人员组成情况
    JZXZRY("JZXZRY"),

    //矫正解除（终止）类型
    JZZZLX("JZZZLX"),

    //家庭经济状况
    JTJJQK("JTJJQK"),

    //目前诊断类型
    MQZDLX("MQZDLX"),

    //吸毒管控情况
    XDGKQK("XDGKQK"),

    //吸毒原因
    XDYY("XDYY"),

    //重点青少年类型
    ZDQSNLX("ZDQSNLX"),

    //重点青少年家庭情况
    ZDQSNJTQK("ZDQSNJTQK"),

    //重点青少年帮扶手段
    ZDQSNBFSD("ZDQSNBFSD"),

    //人口卡片
    RKKP("RKKP"),

    //治安突出问题
    ZATCWT("ZATCWT"),

    //评估效果
    PGXG("PGXG"),

    //办学类型
    BXLX("BXLX"),

    //人员危害程度
    RYWHCD("RYWHCD"),

    //案（事）件性质
    AJXZ("AJXZ"),

    //证件类型
    ZJLX("ZJLX"),

    //线路类型
    XLLX("XLLX"),

    //治安隐患等级
    ZAYHDJ("ZAYHDJ"),

    //事件等级
    SJDJ("SJDJ"),

    //事件满意程度
    SJMYCD("SJMYCD"),

    //企业类型
    QYLX("QYLX"),

    //请假原因
    QJYY("QJYY"),

    //笔记类型
    NOTE_TYPE("NOTE_TYPE"),

    //三会一课的Tab
    HYLX("HYLX"),

    //来华目的
    LHMD("LHMD"),


}