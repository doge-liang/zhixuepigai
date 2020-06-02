window.onload = function () {
    var drag = document.getElementById("drag");
    drag.style.left = "99px";
    drag.onmousedown = function (e) {
        var ol = e.clientX - drag.offsetLeft;
		var left = 0;
        document.onmousemove = function (e) {
			left = e.clientX - ol;
			if (left<99){
				left = 99;
			}
			if (left>=300){
				left = 300;
				check();
				drag.style.left = "99px";
				this.onmousemove = null;//移除移动事件
				this.onmousedown = null;
			}
			else{
				drag.style.left = left+"px";
			}
		}
  }
    
	drag.onmouseup = function () {
		drag.style.left = "99px";
        document.onmousemove = null;
        document.onmousedown = null;
        document.onmouseup = null;
	}
	
	function check(){
		var name = document.forms["form"]["name"].value;
		var password = document.forms["form"]["password"].value;
		if (name == null || name == "") {
			alert("请输入用户名。");
			return false;
		}
		if (password == null || password == "") {
			alert("请输入密码。");
			return false;
		}
		else{
			// java数据库查询函数
			alert("用户名或密码错误");
			return false;
		}
	}
}


