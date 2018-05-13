var serverHeader = "http://localhost:8090/";
$(document).ready(function () {
    /**
     * 导航切换事件
     */
    $("nav .navbar-nav").on("click", "li", function () {
        if (!$(this).hasClass("active")) {
            // 隐藏前一个页面
            $("nav .navbar-nav li").removeClass("active");
            $(".container>div").css("display", "none");

            // 显示当前选中页面
            $(this).addClass("active");
            $(".container ." + $(this).attr("data-toggle")).css("display", "block");
        }
    });

    // 初始化第一页
    $("nav .navbar-nav li:first a").click();

    /**
     * 登录点击事件
     */
    $("#loginRow .input-group>.input-group-btn>button").on("click", function () {
        var ukey = $("#loginRow .input-group>input").val();
        if (ukey) {
            $.ajax({
                async: false,
                url: serverHeader + "seal/login",
                type: "get",
                data: {
                    "ukey": ukey
                },
                dataType: "json",
                success: function (msg) {
                    console.log(msg);
                    if (msg.status == 0) {
                        $("#loginResult").html("登录成功！" + JSON.stringify(msg) + "<br/>");
                        getSelfInfo(function (msg) {
                            $("#loginResult").append("用户信息：" + JSON.stringify(msg) + "<br/>");
                        }, function (err) {
                            $("#loginResult").append("获取用户信息失败：" + JSON.stringify(err) + "<br/>");
                        });
                    }
                    else {
                        $("#loginResult").html("登录失败！" + JSON.stringify(msg) + "<br/>");
                    }
                }, error: function (err) {
                    console.log(err);
                    $("#loginResult").html("调用登录接口失败！" + JSON.stringify(err));
                }
            });
        } else {
            alert("请输入UKey");
        }
    });

    /**
     * 获取登录后用户信息
     * 需要先调用登录接口验证账户
     */
    function getSelfInfo(callback, errorback) {
        $.ajax({
            async: false,
            url: serverHeader + "seal/getSelfInfo",
            type: "get",
            dataType: "json",
            success: function (msg) {
                console.log(msg);
                callback(msg);
            }, error: function (err) {
                console.log(err);
                errorback(err);
            }
        });
    }

    /**
     * 获取印章信息点击事件
     */
    $("#sealInfoRow button").on("click", function () {
        $.ajax({
            async: false,
            url: serverHeader + "seal/getSealInfo",
            type: "get",
            dataType: "json",
            success: function (msg) {
                console.log(msg);
                $("#sealInfoResult").html("获取成功！" + JSON.stringify(msg) + "<br/>");
            }, error: function (err) {
                console.log(err);
                $("#sealInfoResult").html("调用获取印章信息接口失败！" + JSON.stringify(err));
            }
        });
    });

    // 定义签章相关域
    var signType, files, filename;

    /**
     * 选择签章类型点击事件
     */
    $("#signType .input-group-btn .dropdown-menu").on("click", "li", function (evt) {
        signType = $(this).children("a").attr("signType");
        $("#signType>input").val($(this).children("a").html());
    });

    /**
     * 上传文件点击事件
     */
    $("#loadFileDiv>button").on("click", function () {
        if ($("#loadFileDiv>input").val() == "" && $("#selectFile").val() != "") $("#selectFile").val("");
        $("#selectFile").trigger("click");               // 触发文件选择input的click事件
    });

    /**
     * 隐藏的file input对象改变事件
     */
    $("#selectFile").on("change", function (evt) {
        files = this.files;                                                                                        // 选择的文件列表
        filename = files[0].name;                                                                                   // 文件名
        $("#loadFileDiv>input").val(filename);
    });

    /**
     * 签章点击事件
     */
    $("#btnSign").on("click", function () {
        if (!signType) return;

        var sealID = $("#txtSealID").val();
        if (!sealID) return;
        var posCoord = $("#txtPosCoord").val();
        if (!posCoord) return;
        var posArray = posCoord.split(" ");
        switch (signType) {
            case "fileStreamSign": {
                if (files.length > 0) {
                    var fileType = filename.substring(filename.lastIndexOf(".") + 1).toLowerCase();
                    if (fileType == "pdf") {
                        $("#loadFileForm").ajaxSubmit({
                            url: serverHeader + "seal/localSignPDF",
                            type: "post",
                            data: {
                                "param": {
                                    "sealId": sealID,
                                    "posType": "0",
                                    "posPage": "1",
                                    "posX": posArray[0],
                                    "posY": posArray[1]
                                }
                            },
                            dataType: "json",
                            // contentType: "application/x-www-form-urlencoded; charset=utf-8",
                            success: function (msg) {
                                console.log(msg);
                            }, error: function (err) {
                                console.log(err);
                            }
                        });
                    }
                    else {
                        // 转换格式
                    }
                }
                break;
            }
            case "fileSign": {
                var inputFile = $("#txtInputFile").val();
                var outputFile = $("#txtOutputFile").val();
                if (!inputFile || !outputFile) return;

                var inputFileType = inputFile.substring(inputFile.lastIndexOf(".") + 1).toLowerCase();
                var outputFileType = outputFile.substring(outputFile.lastIndexOf(".") + 1).toLowerCase();
                if (inputFileType != outputFileType) return;

                if (inputFileType == "pdf") {
                    var jsonObject = {
                        "signType": "1",
                        "sealId": sealID,
                        "srcPdfFile": inputFile,
                        "dstPdfFile": outputFile,
                        "posType": "0",
                        "posPage": "1",
                        "posX": posArray[0],
                        "posY": posArray[1]
                    };
                    $.ajax({
                        url: serverHeader + "seal/localSignPDF",
                        type: "post",
                        data: JSON.stringify(jsonObject),
                        dataType: "json",
                        contentType: "application/json; charset=utf-8",
                        success: function (msg) {
                            console.log(msg);
                        },
                        error: function (err) {
                            console.log(err);
                        }
                    });
                }
                else {
                    // 转换格式
                }
                break;
            }
        }
    });

    $("#btnGetSignInfo").on("click", function () {
        $.ajax({
            async: false,
            url: serverHeader + "seal/getSignInfoForPDF",
            data: {filePath: $("#txtOutputFile").val()},
            type: "get",
            dataType: "json",
            success: function (msg) {
                console.log(msg);
            }, error: function (err) {
                console.log(err);
            }
        });
    });

    /**
     * 获取证照模板信息点击事件
     */
    $("#btnGetLicense").on("click", function (evt) {
        $.ajax({
            async: false,
            url: serverHeader + "inspur/getLicenceTypeByItemCode",
            data: {itemCode: "39a5dc14-8833-4d53-ae4c-ec155817a20a"},
            type: "get",
            dataType: "json",
            success: function (msg) {
                console.log(msg);
            }, error: function (err) {
                console.log(err);
            }
        });
    });

    /**
     * 获取模板照面信息点击事件
     */
    $("#btnGetPaginateSurface").on("click", function (evt) {
        $.ajax({
            async: false,
            url: serverHeader + "inspur/getPaginateSurfaceByType",
            data: {
                licenseTypeCode: "1c877391-d0d4-4554-adbe-df291c120efc",
                holderType: "natural",
                itemCode: "39a5dc14-8833-4d53-ae4c-ec155817a20a"
            },
            type: "get",
            dataType: "json",
            success: function (msg) {
                console.log(msg);
            }, error: function (err) {
                console.log(err);
            }
        });
    });

    /**
     * 获取证照分页列表信息点击事件
     */
    $("#btnGetLicenceList").on("click", function (evt) {
        $.ajax({
            async: false,
            url: serverHeader + "inspur/getLicenceListByPagination",
            data: {queryMap: "{LICENSE_TYPE_CODE:1c877391-d0d4-4554-adbe-df291c120efc}"},
            type: "get",
            dataType: "json",
            success: function (msg) {
                console.log(msg);
            }, error: function (err) {
                console.log(err);
            }
        });
    });

    /**
     * 获取证照信息及附件点击事件
     */
    $("#btnGetLicenceInfoAndFile").on("click", function (evt) {
        $.ajax({
            async: false,
            url: serverHeader + "inspur/getLicenceInfoAndFile",
            data: {licenseNo: "001008009005013_1c877391-d0d4-4554-adbe-df291c120efc_1_地字第（2016）027号"},
            type: "get",
            dataType: "json",
            success: function (msg) {
                console.log(msg);
            }, error: function (err) {
                console.log(err);
            }
        });
    });

    /**
     * 获取项目信息点击事件
     */
    $("#btnGetProjectInfo").on("click", function (evt) {
        $.ajax({
            async: false,
            url: serverHeader + "inspur/getProjectInfo",
            data: {
                projectCode: "2018-330902-70-02-020404-000"
            },
            type: "get",
            dataType: "json",
            timeout: 2000,
            success: function (msg) {
                console.log(msg);
            }, error: function (err) {
                console.log(err);
            }
        });
    });
});