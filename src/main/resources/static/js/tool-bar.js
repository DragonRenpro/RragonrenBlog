class toolBar{
    // 按钮是否被固定
    fixed_button = false;
    constructor(){
        // 绑定菜单按钮 tool-bar-menu
        document.getElementsByClassName("tool-bar-menu")[0].onclick = this.toggle_menu;
        // 绑定菜单栏上的关闭按钮 close-button
        document.getElementsByClassName("close-button")[0].onclick = this.close_menu;
        // 绑定菜单栏上的固定按钮 fixed-button
        document.getElementsByClassName("fixed-button")[0].onclick = this.fixed_menu;
    }
    // 取消固定
    unfixed_menu() {
        tool_bar.fixed_button = false;
        // 还原背景颜色
        document.getElementsByClassName("fixed-button")[0].style.backgroundColor = "#00000000";
        // 还原按钮绑定
        document.getElementsByClassName("fixed-button")[0].onclick = tool_bar.fixed_menu;
    }
    // 按钮被固定
    fixed_menu(){
        tool_bar.fixed_button = true;
        // 设置背景颜色
        document.getElementsByClassName("fixed-button")[0].style.backgroundColor = "8F8EB5";
        // 修改按钮绑定
        document.getElementsByClassName("fixed-button")[0].onclick = tool_bar.unfixed_menu;
    }
    // 关闭菜单按钮
    close_menu() {
        document.getElementsByClassName("tool-bar-menu")[0].onclick = tool_bar.toggle_menu;
        // 还原圆角
        document.getElementById("tool-bar").style.borderRadius = "0, 0, 10px, 10px";
        if(tool_bar.fixed_button){
            tool_bar.fixed_toast();
            return;
        }
        let menu_side = document.getElementsByClassName("tool-bar-left")[0];
        menu_side.style.width = "0";
        // 清除菜单栏上的点击事件
        document.onclick = null;
    }
    // 展开菜单函数
    toggle_menu() {
        document.getElementsByClassName("tool-bar-menu")[0].onclick = null;
        // 将#tool-bar的圆角修改为0
        document.getElementById("tool-bar").style.borderRadius = "0";
        let menu_side = document.getElementsByClassName("tool-bar-left")[0];
        menu_side.style.width = "200px";
        // 在菜单栏展开期间点击任意不是菜单栏的地方均收回菜单栏
        setTimeout(function () {
            document.onclick = function (event) {
                // 判断位置在不在菜单栏
                // 获取点击的节点
                let target = event.target;
                if(target.className === "tool-bar-left"){
                    return;
                }
                // 获取父单位
                while(target.parentNode != null){
                    target = target.parentNode;
                    if(target.className === "tool-bar-left"){
                        return;
                    }
                }
                tool_bar.close_menu();
            }
        }, 1);
    }
    // 绑定按钮晃动提醒
    fixed_toast(){
        // 播放 fixed_button 动画
        document.getElementsByClassName("fixed-button")[0].classList.add("shake-animation");
        setTimeout(function () {
            document.getElementsByClassName("fixed-button")[0].classList.remove("shake-animation");
        }, 800);
    }
}

// 初始化
const tool_bar = new toolBar();
