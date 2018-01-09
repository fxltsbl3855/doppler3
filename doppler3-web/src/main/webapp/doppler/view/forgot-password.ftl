<!DOCTYPE html>
<html>
<head>
<#include "doppler/common_doppler.ftl">
<link rel="stylesheet" type="text/css"
	href="${base}/doppler/style/css/theme.css">
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
										<font>分布式调用跟踪系统</font>
									</div>
								</header>
								<div id="login-box-inner" class="with-heading">
									<h4>忘记密码？</h4>
									<p>请输入邮箱地址重设密码</p>
									<form role="form" action="index.html">
										<div class="input-group reset-pass-input">
											<span class="input-group-addon"><i
												class="glyphicon glyphicon-envelope"></i></span> <input class="form-control"
												type="text" placeholder="请输入邮箱地址">
										</div>
										<div class="row">
											<div class="col-xs-12">
												<button type="submit" class="btn btn-success col-xs-12">重设密码</button>
											</div>
											<div class="col-xs-12">
												<br /> <a href="login.shtml" id="login-forget-link"
													class="forgot-link col-xs-12">返回登录</a>
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
</body>
</html>