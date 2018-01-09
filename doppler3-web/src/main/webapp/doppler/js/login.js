$(function(){
   //错误提示
});

var Login = function(){
	return{
		
		/**
		 * 回车登录
		 */
		keyDown : function (){
			if(window.event.keyCode == 13){
				if (document.all('login')!=null){
					document.all('login').click();
				}
			}
		},
		
		/**
		 * 登录
		 */
		login : function(){
			if ($("#username").val() == ""){
				$('#username').focus();
				$("#errorMsg").empty().append("<div class='alert alert-error'>用户名不能为空！</div>");
				return;
			} else if ($("#password").val() == ""){
				$('#password').focus();
				$("#errorMsg").empty().append("<div class='alert alert-error'>密码不能为空！</div>");
				return;
			} 
			$("#loginForm").submit();
		},
		
		/**
		 * 注销
		 */
		logout : function(){
			layer.confirm('确定退出?', {icon: 3, title:'提示'} , function(){ 
				var url = $.modulePath + "/view/logout.shtml";
				$.ajax({
					url : url,
					type : 'post',
					success : function(result) {
						try{
							window.location.href = $.modulePath + "/view/login.shtml";
						}catch(err){
						}
					}
				});
			});
		}
	};
}();