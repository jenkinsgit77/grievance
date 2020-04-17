var chatFromRight = "20";
var sFont = "font-family:Verdana,Arial,sans-serif;font-size:.8em;";
var sColor = "background: #3498db;color: #ffffff;";
var TL_ChatTitle = "Talisma Chat";
var ChatWindow_Height = 450;
var ChatWindow_Width = 370;
var TL_MsgFormId = "Talisma";
//******** Same page Chat Window UI Start

function LoadStyle() {
    var cStyle = document.createElement("style");
    cStyle.type = "text/css";
    var strStyle = ".chat-frame {width: " + ChatWindow_Width + "px;border: 0px none;}";
    strStyle += ".bordert{box-shadow:5px 5px 3px rgba(38, 35, 35, 0.58);border:1px solid rgba(38, 35, 35, 0.30);}";
    strStyle += ".chat-frame>div{float:left;}";
    strStyle += "html{" + sFont + "}";
    strStyle += ".transitions{-webkit-transition: all 0.2s ease-in-out 0.2s;-moz-transition: all 0.2s ease-in-out 0.2s;-o-transition: all 0.2s ease-in-out 0.2s;-ms-transition: all 0.2s ease-in-out 0.2s;transition: all 0.2s ease-in-out 0.2s;}";
    strStyle += ".chat-frame iframe {width: 100%;height: 100%;border: 0px none;}";
    strStyle += ".chat-title {height:30px;width:100%;line-height:25px;cursor:pointer;}";
    strStyle += ".chat-title > div {float:right;}";
    strStyle += ".chat-title > span {float:left;margin:2px 5px;font-weight: bold;}";
    strStyle += ".close{ display: block;cursor: pointer;float: right;height: 30px;width: 33px;font-size: 15px !important;line-height: 28px;text-align: center;box-shadow: -1px 0 0px 0 rgba(0, 0, 0, 0.2); }";
    strStyle += ".close:hover {background-color: #ffffff;color: #505050;}";
    strStyle += ".chat-body {width:100%;height: " + (ChatWindow_Height - 30) + "px;}";
    strStyle += "button{text-align:center;padding-left:22px;display: inline-block;width:25px;height:20px;line-height:11px;margin:5px 3px;}";
    strStyle += "button:before{width: 1em;text-align: center;margin-left:-1em;margin-top:1em;pointer-events: none;background:none;background-color:none;}";
    strStyle += "button:hover{Box-shadow: 0 0 2px 3px rgba(0, 0, 0, 0.5);}";
    strStyle += ".absolutes {position: fixed;right: "+ chatFromRight +"px;bottom: 0px;}";
    strStyle += ".bgcolor {"+ sColor +";text-decoration: none;}";
    strStyle += ".btn{padding: 5px 10px 5px 10px;margin:4px;height:31px;width:90px;}";
    strStyle += ".btn:hover {text-shadow: 0 0 15px #000000;}";
    cStyle.innerHTML = strStyle;
    document.body.appendChild(cStyle);
}

var frameDiv = null;
function LoadChatUI() {
    var Var2 = window.Var1;
    frameDiv = document.createElement("div");
    frameDiv.setAttribute("id", "chat-frame");
    frameDiv.setAttribute("class", "chat-frame absolutes bordert");
    var titleDiv = document.createElement("div");
    titleDiv.setAttribute("class", "chat-title bgcolor");
    titleDiv.innerHTML = "<span>" + TL_ChatTitle + "</span>";
    titleDiv.setAttribute("onclick", "minimizeMe()");
    var cBtn = document.createElement("i");
    cBtn.className = "close";
    cBtn.setAttribute("onclick", "closeMe('chat-frame')");
    cBtn.innerHTML = "✖";
    cBtn.setAttribute("class", "close");
    var bDiv = document.createElement("div");
    bDiv.appendChild(cBtn);
    titleDiv.appendChild(bDiv);
    var bodyDiv = document.createElement("div");
    bodyDiv.setAttribute("class", "chat-body transitions");
    bodyDiv.setAttribute("id", "chat-body");
    //bodyDiv.innerHTML = '<iframe id="fChat" src="" name="chat" ></iframe></div>';
    bodyDiv.innerHTML = '<iframe id="fChat" src="'+Var2+'" name="chat" ></iframe></div>';
    frameDiv.appendChild(titleDiv);
    frameDiv.appendChild(bodyDiv);
    document.body.appendChild(frameDiv);
    var frmeHeight = document.getElementById("chat-frame").offsetHeight;
    document.getElementById("chat-body").style.height = (frmeHeight - 30) + "px";
}

function minimizeMe() {
    if (document.getElementById("chat-body")) {
        if (document.getElementById("chat-body").style.height == "0px")
            document.getElementById("chat-body").style.height = (ChatWindow_Height - 30) + "px";
        else
            document.getElementById("chat-body").style.height = 0 + "px";        
    }
}
function closeMe(id) {
    if (document.getElementById(id)) {
        var chatFrame = document.getElementById(id);
        document.body.removeChild(chatFrame);
    }    
}

//******** Same page Chat Window UI End


// To initiate Chat (Proactive/reactive)
function OpenChat(proactive) {

            var url = proactive.getSource().getProperty('talismaURL');

            window.Var1 = url;

            LoadStyle();
            
            LoadChatUI();
    
//     var url = "http://10.58.7.82/IOCLPreChat/PreChat.aspx";
//        var form = document.getElementById(TL_MsgFormId);
//        form.action = url;
//        form.submit();
    }

window.addEventListener("message", receiveMessage, false);

function receiveMessage(event)
{
  if (event.origin == window.location.hostname)
  closeMe('chat-frame');
}