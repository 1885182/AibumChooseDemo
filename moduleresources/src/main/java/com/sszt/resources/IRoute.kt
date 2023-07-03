package com.sszt.resources

/**
 *
 * @param time 2021/6/29
 * @param des 路由
 * @param author Meng
 *
 */
object IRoute {

    const val main_main = "/main/main"
    const val main_signature = "/main/signature"
    private const val LOGIN = "/login"
    private const val MAP = "/map"
    private const val EVENT = "/event"


    //测试
    const val test_test = "/test/test"

    //综治调解
    private const val CASE = "/case"

    //信访业务
    private const val PETITION = "/petition"

    //信访隐患排查
    private const val TROUBLESHOOT = "/troubleshoot"

    //人员帮扶
    private const val ASSIST = "/assist"

    //我的
    private const val MINE = "/mine"

    //民情收集
    private const val SENTIMENT = "/sentiment"

    //登录
    const val login_login = "$LOGIN/login"

    //忘记密码-手机号验证
    const val login_phone = "$LOGIN/forgetPas"

    //忘记密码-修改密码
    const val login_update_pas = "$LOGIN/forgetPas"


    //消息中心
    const val message_fragment = "/message/fragment"
    //通知公告
    const val message_noty = "/message/noty"
    //消息详情
    const val message_detail = "/message/detail"


    //个人中心
    const val mine_fragment = "$MINE/fragment"

    //修改手机号
    const val mine_update_phone = "$MINE/updatePhone"

    //修改密码
    const val update_pas = "$MINE/updatePas"

    //工作台
    const val workbench_fragment = "/workbench/fragment"

    //案件登记
    const val case_register = "$CASE/register"

    //调解机构
    const val case_mediate_institution = "$CASE/mediateInstitution"

    //添加申请人/被申请人
    const val case_insert_proposer = "$CASE/insertProposer"

    //案件登记-提交
    const val case_insert = "$CASE/insert"

    //案件列表
    const val case_list = "$CASE/list"

    //案件详情
    const val case_detail = "$CASE/detail"

    //添加笔录
    const val case_record_word = "$CASE/recordWord"

    //笔录详情
    const val case_record_detail = "$CASE/recordDetail"

    //案件文书添加
    const val case_writ = "$CASE/writ"

    //案件文书详情
    const val case_writ_detail = "$CASE/writDetail"



    //民情收集-添加
    const val sentiment_insert = "$SENTIMENT/insert"

    //民情台账
    const val sentiment_list = "$SENTIMENT/list"

    //民情详情
    const val sentiment_detail = "$SENTIMENT/detail"



    //帮扶人员列表
    const val assist_people_list = "$ASSIST/peopleList"

    //帮扶记录列表
    const val assist_record_list = "$ASSIST/recordList"

    //帮扶记录详情
    const val assist_record_detail = "$ASSIST/recordDetail"

    //帮扶记录新增
    const val assist_record_insert = "$ASSIST/recordInsert"

    //帮扶人员详情
    const val assist_people_detail = "$ASSIST/peopleDetail"




    //信访列表
    const val petition_letter_list = "$PETITION/letterList"

    //信访详情
    const val letter_detail = "$PETITION/letterDetail"

    //信访办理情况
    const val letter_transact = "$PETITION/letterTransact"

    //延期申请
    const val letter_postpone = "$PETITION/letterPostpone"

    //转办交办
    const val letter_manage = "$PETITION/letterManage"

    //作废/复查/复核申请
    const val letter_apply = "$PETITION/letterApply"

    //文书
    const val letter_web_view = "$PETITION/letterWebView"

    //作废/复查/复核审核
    const val letter_check = "$PETITION/letterCheck"

    //信访评价
    const val letter_comment = "$PETITION/letterComment"

    //排查登记-详情
    const val event_detail = "$EVENT/detail"

    //排查登记-列表
    const val event_list = "$EVENT/list"

    //排查登记-新增
    const val event_insert = "$EVENT/insert"

    //新增
    const val view_add = "/test/insert"

    //列表
    const val view_list = "/test/list"

    //信访隐患排查-列表
    const val troubleshooting_list = "$TROUBLESHOOT/list"

    //信访隐患排查-添加
    const val troubleshooting_insert = "$TROUBLESHOOT/insert"

    //信访隐患排查-详情
    const val troubleshooting_detail = "$TROUBLESHOOT/detail"

    //信访隐患排查-评估审核/摘牌申请
    const val assess_check = "$TROUBLESHOOT/assessCheck"

    //信访隐患排查-评估详情
    const val assess_detail = "$TROUBLESHOOT/assessDetail"

    //摘牌详情
    const val pick_detail = "$TROUBLESHOOT/pickDetail"

    //信访隐患排查-摘牌审核
    const val pick_check = "$TROUBLESHOOT/pickCheck"

    //信访隐患排查-挂牌申请/详情
    const val hang_detail = "$TROUBLESHOOT/hangApply"





    /**
     * 选择位置
     */
    const val map_select = "$MAP/select"
    const val map_show_loc = "$MAP/selectShowLoc"

}