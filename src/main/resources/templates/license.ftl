<!DOCTYPE html>
<html lang="en">
<head>
  <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"/>
  <meta charset="utf-8"/>
  <title>license生成</title>

  <meta name="description" content="with selectable elements and custom icons"/>
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0"/>

  <!-- bootstrap & fontawesome -->
  <link rel="stylesheet" href="/assets/css/bootstrap.min.css"/>
  <link rel="stylesheet" href="/assets/font-awesome/4.5.0/css/font-awesome.min.css"/>

  <!-- page specific plugin styles -->

  <!-- text fonts -->
  <link rel="stylesheet" href="/assets/css/fonts.googleapis.com.css"/>

  <!-- ace styles -->
  <link rel="stylesheet" href="/assets/css/ace.min.css" class="ace-main-stylesheet"
        id="main-ace-style"/>

  <!--[if lte IE 9]>
  <link rel="stylesheet" href="/assets/css/ace-part2.min.css" class="ace-main-stylesheet"/>
  <![endif]-->
  <link rel="stylesheet" href="/assets/css/ace-skins.min.css"/>
  <link rel="stylesheet" href="/assets/css/ace-rtl.min.css"/>

  <!--[if lte IE 9]>
  <link rel="stylesheet" href="/assets/css/ace-ie.min.css"/>
  <![endif]-->


  <!-- ace settings handler -->
  <script src="/assets/js/ace-extra.min.js"></script>

  <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

  <!--[if lte IE 8]>
  <script src="/assets/js/html5shiv.min.js"></script>
  <script src="/assets/js/respond.min.js"></script>
  <![endif]-->
</head>

<body class="no-skin">
<div class="main-container ace-save-state" id="main-container">
  <script type="text/javascript">
    try {
      ace.settings.loadState('main-container')
    } catch (e) {
    }
  </script>


  <div class="page-content">
    <div class="col-sm-5 widget-box widget-color-blue2">
      <div class="widget-header">
        <h4 class="widget-title lighter smaller">license生成</h4>
      </div>
      <form class="form-horizontal" id="userform" user="form" style="margin-top: 20px;">
        <input type="hidden" name="id" id="id" value="${(user.id)!}"/>

        <div class="form-group">
          <label class="col-sm-3 control-label no-padding-right"> 用户名称 </label>
          <div class="col-sm-9">
            <input type="text" id="name" name="name" placeholder="用户名称"
                   class="col-xs-10 col-sm-5">
          </div>
        </div>
        <div class="form-group">
          <label class="col-sm-3 control-label no-padding-right"> IP地址 </label>
          <div class="col-sm-9">
            <input type="text" id="ip" name="ip" placeholder="IP地址"
                   class="col-xs-10 col-sm-5">
          </div>
        </div>

        <div class="form-group">
          <label class="col-sm-3 control-label no-padding-right"> MAC地址 </label>
          <div class="col-sm-9">
            <input type="text" id="mac" name="mac" placeholder="MAC地址"
                   class="col-xs-10 col-sm-5">
          </div>
        </div>

        <div class="form-group">
          <label class="col-sm-3 control-label no-padding-right"> CPU序列号 </label>
          <div class="col-sm-9">
            <input type="text" id="cpu" name="cpu" placeholder="CPU序列号"
                   class="col-xs-10 col-sm-5">
          </div>
        </div>

        <div class="form-group">
          <label class="col-sm-3 control-label no-padding-right"> 主板序列号 </label>
          <div class="col-sm-9">
            <input type="text" id="mainBoard" name="mainBoard" placeholder="主板序列号"
                   class="col-xs-10 col-sm-5">
          </div>
        </div>

        <div class="form-group">
          <label class="col-sm-3 control-label no-padding-right"> 有效时间 </label>
          <div class="col-sm-9 dataTables_length">
            <select name="licensetime" class="input-sm">
              <option value="31" <#if user?? && user.flag==1>selected</#if>>31
              </option>
              <option value="45" <#if user?? && user.flag==1>selected</#if>>45
              </option>
              <option value="60" <#if user?? && user.flag==1>selected</#if>>60
              </option>
              <option value="90" <#if user?? && user.flag==1>selected</#if>>90
              </option>
              <option value="120" <#if user?? && user.flag==1>selected</#if>>120
              </option>
              <option value="180" <#if user?? && user.flag==1>selected</#if>>180
              </option>
              <option value="360" <#if user?? && user.flag==1>selected</#if>>360
              </option>
            </select>
          </div>
        </div>

        <div class="form-group">
          <label class="col-sm-3 control-label no-padding-right"> 生成路径 </label>
          <div class="col-sm-9">
            <input type="text" id="licensePath" name="licensePath" placeholder="生成路径"
                   class="col-xs-10 col-sm-5">
          </div>
        </div>

      <#--${authorities?seq_contains("/admin/user/editlicense")?string('<button class="btn btn-info" type="button" style="margin-left: 20px;" onclick="add()">-->
      <#--<i class="ace-icon fa fa-check bigger-110"></i>-->
      <#--生成-->
      <#--</button>','')}-->
      </form>
    </div>
  </div>
</div>
<!--[if !IE]> -->
<script src="/assets/js/jquery-2.1.4.min.js"></script>

<!-- <![endif]-->

<!--[if IE]>
<script src="/assets/js/jquery-1.11.3.min.js"></script>
<![endif]-->
<script type="text/javascript">
  if ('ontouchstart'
      in document.documentElement) {
    document.write("<script src='/assets/js/jquery.mobile.custom.min.js'>"
        + "<" + "/script>");
  }
</script>
<script src="/assets/js/bootstrap.min.js"></script>

<!-- page specific plugin scripts -->
<script src="/assets/js/tree.min.js"></script>

<script src="/assets/js/spinbox.min.js"></script>
<script src="/assets/js/jquery.inputlimiter.min.js"></script>
<script src="/assets/js/jquery.maskedinput.min.js"></script>
<!-- ace scripts -->
<script src="/assets/js/ace-elements.min.js"></script>
<script src="/assets/js/ace.min.js"></script>

<script src="/assets/js/bootbox.js"></script>
<script src="/assets/js/utils.js"></script>
<script>

  function add() {
    var modules = document.getElementsByName('modules');
    var module = new Array();
    for (var i = 0; i < modules.length; i++) {
      if (modules[i].checked) {
        module.push(modules[i].value);
      }
    }
    //alert(module.toString());

    if (!vali($("#name"))) {
      alert("请输入用户名称");
      return;
    }
    if (!vali($("input[name=mac]"))) {
      alert("请输入mac地址");
      return;
    }

    var cascading = 0;
    var temp = $("select[name=cascading]").val();
    if (temp == 1 || temp == 0) {
      cascading = temp;
    }

    quickAjax({
      url: '/admin/user/editlicense',
      method: "POST",
      data: {
        id: $("#id").val(),
        user: $("#name").val(),
        mac: $("#mac").val(),
        licensetime: $("select[name=licensetime]").val(),
        managenode: $("select[name=managenode]").val(),
        module: module.toString(),
        cascading: cascading
      },
      success: function (response) {
        if (response.code == 1) {
          alert("生成成功", function () {
            self.location = document.referrer;
          });

        }
      },
      error: function (response) {
        alert("链接服务器失败");
      }
    });
  }
</script>
</body>

</html>