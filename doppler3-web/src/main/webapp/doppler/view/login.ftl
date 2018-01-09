<!DOCTYPE html>
<html>
<head>
<#include "doppler/common_doppler.ftl">
<link rel="stylesheet" type="text/css" href="${base}/doppler/style/css/theme.css">
<style type="text/css">
font{
	font-family: '楷体';
	font-size: xx-large;
};
</style>
</head>
<body id="login-page">
	<div class="container">
		<div class="row">
			<div class="col-xs-12">
				<div id="login-box">
					<div id="login-box-holder">
						<div class="row">
							<div class="col-xs-12">
								<header id="login-header">
									<div id="login-logo">
										<font>分布式调用系统</font>
									</div>
								</header>
								<div id="login-box-inner">
									<form role="form" id="loginForm" action="<@modulePath/>/view/dashboard.shtml" method="post" onkeydown="Login.keyDown()">
										<div class="input-group">
											<span class="input-group-addon"><i class="glyphicon glyphicon-user"></i></span>
											<input id="username" name="username" type="text" class="form-control" placeholder="用户名" required="required">
										</div>
										<div class="input-group">
											<span class="input-group-addon"><i class="glyphicon glyphicon-lock"></i></span>
											<input id="password" name="password" type="password" class="form-control" placeholder="密码">
										</div>
										<div id="remember-me-wrapper">
											<div class="row">
<!-- 												<div class="col-xs-6"> -->
<!-- 													<div class="checkbox-nice"> -->
<!-- 														<input type="checkbox" id="remember-me" checked="checked" /> -->
<!-- 														<label for="remember-me"> 记住账号 </label> -->
<!-- 													</div> -->
<!-- 												</div> -->
<!-- 												<a href="forgot-password.shtml" id="login-forget-link" -->
<!-- 													class="col-xs-6"> 忘记密码? </a> -->
											</div>
										</div>
										<div class="row">
											<div class="col-xs-12">
												<button id="login" class="btn btn-success col-xs-12" type="button" onclick="Login.login();">登录</button>
											</div>
											<#if message??>
							        		<div id="errorMsg" style="color:red" >
							        			${message}
							        		</div>
							        		<#else>
							        		<div id="errorMsg" style="color:red" >
							        		</div>
							        		</#if>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
<!-- 					<div id="login-box-footer"> -->
<!-- 						<div class="row"> -->
<!-- 							<div class="col-xs-12"> -->
<!-- 								还没有账号？ <a href="registration.html"> 立即注册 </a> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
				</div>
			</div>
		</div>
	</div>

<script type="text/javascript" src="${base}/doppler/js/login.js"></script>
</body>
</html>