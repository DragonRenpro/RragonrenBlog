
class SimpleAnimation{
    constructor() {

    }
    init_toast() {
        // 为所有添加 class = js-simple-toast 的组件添加一个鼠标悬停提示框
        let simple_toast_components = document.getElementsByClassName("js-simple-toast");
        for (let i = 0; i < simple_toast_components.length; i++) {
            simple_animation.simple_toast(simple_toast_components[i], i);
        }
    }
    simple_toast(element, i){
        // 生成一个唯一的 id 用来标识
        let id = "simple-toast-" + i;
        element.onmouseover = () => {
            // 获取元素的 toast_text 属性
            let toast_text = element.getAttribute("toast_text");
            // 当鼠标悬停时在上方插入一个小框框
            let toast_box = document.createElement("div");
            toast_box.className = "simple-toast-box";
            toast_box.id = id;
            toast_box.innerHTML = toast_text;
            let position = element.getBoundingClientRect();
            // 特殊情况 如果元素上方没有足够的空间就定位到元素下方
            if (position.top - position.height - toast_box.offsetHeight - 10  <= 0) {
                toast_box.style.top = position.top + position.height + toast_box.offsetHeight + 10 + "px";
                toast_box.style.left = position.left + (toast_box.clientWidth * 0.5) - 5 + "px";
            }else{
                // 定位到元素上方
                toast_box.style.left = position.left + (toast_box.clientWidth * 0.5) - 5 + "px";
                toast_box.style.top = position.top - position.height - toast_box.offsetHeight - 10 + "px";
            }

            // 添加在其父节点后
            element.parentNode.insertBefore(toast_box, element.nextSibling);
            setTimeout(() => {
                    toast_box.style.opacity = "1";
                    }, 100);
        };
        element.onmouseout = () => {
            let element = document.getElementById(id);
            element.style.opacity = "0";
            // 将插入的插入框消失
            setTimeout(() => {
                element.remove();
            }, 500)

        };
    }
}

const simple_animation = new SimpleAnimation();
simple_animation.init_toast();