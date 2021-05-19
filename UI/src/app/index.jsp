<!-- Author: Thanga Vignesh Raja T
     Date: 09/11/2016
         Version: 1.0.0
-->
<%@page import="java.util.Map"%>
    <%@page import="com.tsi.workflow.Constants"%>
        <%@page import="com.tsi.workflow.AppDetails"%>
            <%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
                <%@page contentType="text/html" pageEncoding="windows-1252"%>
                    <!DOCTYPE html>
                    <html>

                    <head>
                        <title>zTPF DevOps Toolchain</title>
                        <meta name="description" content="zTPF DevOps Toolchain Application" />
                        <meta charset="utf-8" />
                        <meta http-equiv="X-UA-Compatible" content="IE=edge" />
                        <meta http-equiv="X-UA-Compatible" content="IE=9, IE=10, IE=11" />
                        <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
                        <meta http-equiv="cache-control" content="max-age=0" />
                        <meta http-equiv="cache-control" content="no-cache" />
                        <meta http-equiv="cache-control" content="no-store" />
                        <meta http-equiv="expires" content="0" />
                        <meta http-equiv="expires" content="-1" />
                        <meta http-equiv="expires" content="Tue, 01 Jan 1980 1:00:00 GMT" />
                        <meta http-equiv="pragma" content="no-cache" />

                        <meta http-equiv="Pragma" content="no-cache">
                        <meta http-equiv="Cache-Control" content="no-cache">
                        <meta http-equiv="Expires" content="Sat, 01 Dec 2001 00:00:00 GMT">
                        <link rel="icon" href="favicon.png" type="image/png" sizes="16x16">

                        <!-- Page on Load-->
                        <style>
                            html {
                                background: #fff;
                                padding: 0;
                                margin: 0;
                                height: 100%;
                            }

                            .load {
                                position: absolute;
                                top: calc(50% - 30px);
                                left: calc(50% - 30px);
                                width: 70px;
                                height: 70px;
                            }

                            .block {
                                position: relative;
                                height: 20px;
                                width: 20px;
                                display: inline-block;
                                background: #027ab0;
                                transition: all 0.8s;
                                animation: rot 5s linear infinite;
                            }

                            .block:nth-child(1) {
                                animation-delay: 3s;
                            }

                            .block:nth-child(2) {
                                animation-delay: 1.5s;
                                animation: rot 15s linear infinite;
                            }

                            .block:nth-child(3) {
                                animation-delay: 2s;
                            }

                            .block:nth-child(4) {
                                animation-delay: 0.2s;
                            }

                            .block:nth-child(5) {
                                animation-delay: 4s;
                            }

                            .block:nth-child(6) {
                                animation-delay: 2s;
                                animation: rot 7s linear infinite;
                            }

                            .block:nth-child(7) {
                                animation-delay: 0.4s;
                            }

                            .block:nth-child(8) {
                                animation-delay: 1.5s;
                                animation: rot 6s linear infinite;
                            }

                            .block:nth-child(9) {
                                animation-delay: 25s;
                                animation: rot 8s linear infinite;
                            }

                            @keyframes rot {
                                0% {
                                    transform: none;
                                }
                                20% {
                                    transform: rotateZ(-90deg) rotateY(180deg);
                                }
                                40% {
                                    background: #228c8a;
                                    transform: none;
                                }
                                60% {
                                    background: #56aa45
                                }
                                80% {
                                    background: #57c2c9;
                                }
                                90% {
                                    transform: none;
                                    background: #aed87a;
                                }
                            }

                            .loadScreen {
                                width: 100%;
                                height: 100vh;
                                /*background: rgba(0, 0, 0, 0.32);*/
                                position: fixed;
                                z-index: 9999;
                                cursor: wait;
                            }
                        </style>
						<% Map lAppBuildDetail = (Map) AppDetails.getManifestDetail(request);%>
                        <!--CSS-->
                        <link href="plugins/angular-material/angular-material.min.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />

                        <link href="css/bootstrap.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="css/site.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="css/style.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="css/nga.all.min.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="css/themes.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="https://fonts.googleapis.com/css?family=Source+Sans+Pro:400,700,900" rel="stylesheet">
                        <link href="css/dashboard.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="css/font-awesome.min.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <!-- build:css css/styles.min.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%> -->
                        <link href="plugins/bootstrap-slider/slider.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="css/flexslider.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="plugins/owl-carousel/owl.theme.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="plugins/owl-carousel/owl.transitions.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="plugins/owl-carousel/owl.carousel.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="plugins/select2/css/select2.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <!--<link href="plugins/ui-select/ui-select.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />-->
                        <link href="plugins/pace/pace.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="plugins/animate/animate.min.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="plugins/progress/bootstrap-progressbar.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <!--<link href="plugins/jquery-ui.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />-->
                        <!--
Datatable
        -->
                        <link href="plugins/datatable/dataTables.bootstrap4.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="plugins/datatable/dataTables.responsive.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <!--
Date Time Picker
        -->
                        <link href="plugins/timepicker/angular-moment-picker.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <!-- Full Calendar    -->
                        <link href="plugins/full-calendar/zabuto_calendar.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <!-- Tag Input    -->
                        <link href="plugins/jquery-tags/jquery.tagit.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <link href="plugins/jquery-tags/tagit.ui-zendesk.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <!-- Bootstrap Multiselect    -->
                        <link href="plugins/multiselect/bootstrap-multiselect.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet" />
                        <!-- endbuild -->
                        <link href="plugins/Toaster/iziToast.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet">

                        <link href="plugins/mask/intlTelInput.css?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>" rel="stylesheet">

                    </head>

                    <body ng-app="app" class=" background-container hold-transition sidebar-mini {{theme}} {{layout}}" style="overflow-y:hidden;">
                        <div class="loadScreen">
                            <div class="load">
                                <div class="blockcont">
                                    <div class="block"></div>
                                    <div class="block"></div>
                                    <div class="block"></div>

                                    <div class="block"></div>
                                    <div class="block"></div>
                                    <div class="block"></div>

                                    <div class="block"></div>
                                    <div class="block"></div>
                                    <div class="block"></div>

                                </div>
                            </div>
                        </div>
                        <!-- build:js js/scripts.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%> -->
                        <!--Scripts-->
                        <script src="js/core/jquery.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/config.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script>
                            if (!localStorage.getItem("default_timezone")) {
                                localStorage.setItem("default_timezone", appConfig.timezone)
                            }
                        </script>
                        <script src="js/utils.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script>
                            (function () {
                                $(window).on("load", function () {
                                    $(".loadScreen").delay().fadeOut(3000);
                                });
                            })()
                                <% String lAppdetail = (String) request.getSession().getAttribute("AppHeaders");%>
                            var appSession = JSON.parse(escapeSpecialChars('<%= lAppdetail %>'));
                            localStorage.setItem("401", null)
                        </script>
                        <script src="js/core/bootstrap.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/jquery.cookie.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/mustache.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/angular.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/angular-sanitize.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/angular-route.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/angular-animate.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/angular-messages.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/angular-cookie.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/angular-ui-router-min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/angular-resource.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/knob/jquery.knob.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/Chart.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/owl-carousel/owl.carousel.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/underscore.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/FileSaver.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/select2/js/select2.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/jquery-ui.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/moment/moment.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/mask/intlTelInput.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/mask/utils.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/mask/ng-intl-tel-input.module.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/mask/ng-intl-tel-input.provider.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/mask/ng-intl-tel-input.directive.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/atmosphere.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <!--
Date Time Picker
        -->
                        <!--
Datatable
        -->
                        <script src="plugins/datatable/jquery.dataTables.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/datatable/dataTables.bootstrap.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/datatable/dataTables.buttons.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/datatable/buttons.bootstrap.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/datatable/dataTables.select.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/datatable/dataTables.responsive.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/paginate/dirPagination.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <!-- Angular Material Library -->
                        <script src="js/core/angular-material.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/angular-aria.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/angular-messages.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/moment-with-locales.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/moment-with-locales-with-data.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/timepicker/angular-moment-picker.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/angular-flash.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/angular-sanitize.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/core/ui-bootstrap-tpls-0.13.0.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <!-- Full Calendar    -->
                        <script src="plugins/full-calendar/zabuto_calendar.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <!-- File Upload    -->
                        <script src="plugins/fileUpload/ng-file-upload.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/fileUpload/ng-file-upload-shim.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <!-- Tag Input    -->
                        <script src="plugins/jquery-tags/tag-it.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <!-- Tag Input    -->
                        <script src="plugins/multiselect/bootstrap-multiselect.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/progress/bootstrap-progressbar.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="plugins/Toaster/iziToast.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <!--For Promise Function-->
                        <script src="plugins/polyfill.min.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
 						<script src="plugins/ng-infinite-scroll.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
 						<script src="plugins/ng-pattern-restrict.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <!-- endbuild -->

                        <!--<script src="plugins/logger/logger.service.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>-->

                        <!--Common-->

                        <!--main app-->
                        <script src="js/app.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="js/appCtrl.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="factory/Access.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="factory/validateFields.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="factory/APIFactory.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="services/Toaster.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="services/apiService.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="services/FreezeService.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="services/WSService.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="services/Logger.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="services/IPService.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="services/IService.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="services/RService.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="services/Paginate.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>

                        <!--Login Module-->
                        <script src="js/loginMgmt.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/loginCtrl.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>

                        <!--Dashboard Module-->
                        <script src="js/dashboardApplication.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <!-- Controllers -->
                        <!--<script src="app/directives/autocomplete.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>-->
                        <script src="controllers/settings.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/impPlan.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/newImpPlan.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/impl.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/updateImp.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <!-- <script src="controllers/git.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script> -->
                        <script src="controllers/newImp.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/updateImpPlan.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/freezeDate.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/loadCategories.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/putLevelDD.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/search.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/myTasks.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/reviewTasks.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/prTaskHistory.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/devManager.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/assignedPlan.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/fallbackMacroPlan.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/moveArtifacts.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/dtnMaintenance.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/leadDeployment.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/qaDeployment.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/leadAuxDeploymentScreen.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/passedQARegression.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/qaAuxDeploymentScreen.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/tssAuxDeployment.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/plansDeployedInPreProd.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/defaultcpu.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/delegation.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/loadsetReady.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/loadHistory.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/manageDelegate.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/createNewRepo.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/repositoryOwners.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/tssDeploy.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/help.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/syncImplementationPlan.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/activateFallback.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/acceptFallback.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/accessPermisssion.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/qaMytask.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/utilitiesReport.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/projNbrDisplay.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/reportGenerate.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/rfcChangeMgmt.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/rfcApprovedPlans.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/onlineFeedback.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/buildQueue.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/transactionView.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>
                        <script src="controllers/systemView.js?ver=<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION) + "_" + lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>"></script>

                        <script>
                            window.onload = function () {
                                //GetClock();
                                setInterval(GetClock, 1000);
                            }
                        </script>

                        <div flash-message="5000"></div>
                        <div id="sweModal" class="modal fade" style="z-index:9999;">
                            <div class="modal-dialog">
                                <div class="modal-content sweContent" style="width: 50%;margin: 50% auto;padding: 20px;background:#aaa;">
                                    <div class="sweModal-body">
                                        <div class="row" style="height:50px;text-align: center;">
                                            <!--<i class=""></i>-->
                                            <!--<i class=""></i>-->
                                            <i class="sweIcon" style="color:white;"></i>
                                        </div>
                                        <div class="row" style="text-align:center;">
                                            <h4 class="sweModal-content" style="color:white;word-wrap: break-word;text-align: center;"></h4>
                                        </div>
                                        <div class="row" style="text-align:center;">
                                            <button type="button" class="btn btn-primary okBtn" data-dismiss="modal" style="border-radius: 10px;background: none;color: #fff;line-height: 10px;border-color: #fff;">
                                                Ok
                                            </button>
                                        </div>
                                    </div>

                                </div>
                                <!-- /.modal-content -->
                            </div>
                            <!-- /.modal-dialog -->
                        </div>

                        <ui-view></ui-view>

                            <script>
                                var appBuildInformation = {
                                    "appName": '<%=lAppBuildDetail.get(Constants.APPLICATION_NAME)%>',
                                    "buildYear": '<%= lAppBuildDetail.get(Constants.APPLICATION_BUILD_TIME).toString().substring(0, 4)%>',
                                    "orgName": '<%= lAppBuildDetail.get(Constants.ORGANISATION_NAME)%>',
                                    "appVersionName": '<%= lAppBuildDetail.get(Constants.APPLICATION_VERSION)%>',
                                    "appVersion": '<%= lAppBuildDetail.get(Constants.APPLICATION_BUILD)%>',
                                    "buildDateTime": '<%= lAppBuildDetail.get(Constants.APPLICATION_BUILD_TIME)%>',
                                    "isSSOApp": <%= Constants.isSSOApp%>
                                }
                                localStorage.setItem("appInfo", JSON.stringify(appBuildInformation))
                            </script>


                    </body>

                    </html>



