import{d as e,g as s,u as a,t as l,r as i,a as o,b as t,_ as r,c,e as d,w as n,v as p,f as u,h,i as m,j as f,o as v}from"./index-2741d3dc.js";import{m as x}from"./messageBox-46718661.js";const g="function"==typeof atob,w="function"==typeof btoa,C="function"==typeof Buffer,y="function"==typeof TextDecoder?new TextDecoder:void 0,b="function"==typeof TextEncoder?new TextEncoder:void 0,A=Array.prototype.slice.call("ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="),T=(e=>{let s={};return A.forEach(((e,a)=>s[e]=a)),s})(),k=/^(?:[A-Za-z\d+\/]{4})*?(?:[A-Za-z\d+\/]{2}(?:==)?|[A-Za-z\d+\/]{3}=?)?$/,V=String.fromCharCode.bind(String),U="function"==typeof Uint8Array.from?Uint8Array.from.bind(Uint8Array):e=>new Uint8Array(Array.prototype.slice.call(e,0)),B=e=>e.replace(/=/g,"").replace(/[+\/]/g,(e=>"+"==e?"-":"_")),F=e=>e.replace(/[^A-Za-z0-9\+\/]/g,""),S=e=>{let s,a,l,i,o="";const t=e.length%3;for(let r=0;r<e.length;){if((a=e.charCodeAt(r++))>255||(l=e.charCodeAt(r++))>255||(i=e.charCodeAt(r++))>255)throw new TypeError("invalid character found");s=a<<16|l<<8|i,o+=A[s>>18&63]+A[s>>12&63]+A[s>>6&63]+A[63&s]}return t?o.slice(0,t-3)+"===".substring(t):o},j=w?e=>btoa(e):C?e=>Buffer.from(e,"binary").toString("base64"):S,E=C?e=>Buffer.from(e).toString("base64"):e=>{let s=[];for(let a=0,l=e.length;a<l;a+=4096)s.push(V.apply(null,e.subarray(a,a+4096)));return j(s.join(""))},N=e=>{if(e.length<2)return(s=e.charCodeAt(0))<128?e:s<2048?V(192|s>>>6)+V(128|63&s):V(224|s>>>12&15)+V(128|s>>>6&63)+V(128|63&s);var s=65536+1024*(e.charCodeAt(0)-55296)+(e.charCodeAt(1)-56320);return V(240|s>>>18&7)+V(128|s>>>12&63)+V(128|s>>>6&63)+V(128|63&s)},W=/[\uD800-\uDBFF][\uDC00-\uDFFFF]|[^\x00-\x7F]/g,D=e=>e.replace(W,N),I=C?e=>Buffer.from(e,"utf8").toString("base64"):b?e=>E(b.encode(e)):e=>j(D(e)),P=(e,s=!1)=>s?B(I(e)):I(e),z=/[\xC0-\xDF][\x80-\xBF]|[\xE0-\xEF][\x80-\xBF]{2}|[\xF0-\xF7][\x80-\xBF]{3}/g,M=e=>{switch(e.length){case 4:var s=((7&e.charCodeAt(0))<<18|(63&e.charCodeAt(1))<<12|(63&e.charCodeAt(2))<<6|63&e.charCodeAt(3))-65536;return V(55296+(s>>>10))+V(56320+(1023&s));case 3:return V((15&e.charCodeAt(0))<<12|(63&e.charCodeAt(1))<<6|63&e.charCodeAt(2));default:return V((31&e.charCodeAt(0))<<6|63&e.charCodeAt(1))}},Z=e=>e.replace(z,M),G=e=>{if(e=e.replace(/\s+/g,""),!k.test(e))throw new TypeError("malformed base64.");e+="==".slice(2-(3&e.length));let s,a,l,i="";for(let o=0;o<e.length;)s=T[e.charAt(o++)]<<18|T[e.charAt(o++)]<<12|(a=T[e.charAt(o++)])<<6|(l=T[e.charAt(o++)]),i+=64===a?V(s>>16&255):64===l?V(s>>16&255,s>>8&255):V(s>>16&255,s>>8&255,255&s);return i},L=g?e=>atob(F(e)):C?e=>Buffer.from(e,"base64").toString("binary"):G,R=C?e=>U(Buffer.from(e,"base64")):e=>U(L(e).split("").map((e=>e.charCodeAt(0)))),_=C?e=>Buffer.from(e,"base64").toString("utf8"):y?e=>y.decode(R(e)):e=>Z(L(e)),H=e=>F(e.replace(/[-_]/g,(e=>"-"==e?"+":"/"))),q=e=>_(H(e)),J=P,K=q;function O(e){return!!e||"您必须输入用户名密码"}const Q=e({data:()=>({username:"",password:"",valiCode:"",pageType:1,id:0,jwt:"",img:"",funId:"",vueName:"",loginCode:"",email:"",messageCode:"",showSlider:!1,remember:!0,role:"STUDENT",perName:"",rules:[O]}),beforeMount(){this.pageType=1},async created(){const e=await s();this.id=e.validateCodeId,this.img=e.img;const l=a();l.remember?(this.username=K(l.usernameSave),this.password=K(l.passwordSave),this.remember=!0):(this.username="",this.password="",this.remember=!1)},methods:{async changeValiCode(){const e=await s();this.id=e.validateCodeId,this.img=e.img,this.valiCode=""},backLogin(){this.username="",this.password="",this.valiCode="",this.pageType=1},forgetPass(){this.username="",this.password="",this.valiCode="",this.pageType=2},pageRegister(){this.username="",this.password="",this.valiCode="",this.pageType=3},async initPassword(){let e=await l({validateCodeId:this.id,validateCode:this.valiCode});if(0!=e.code)return x(this,e.msg),void this.changeValiCode();""!=this.username&&null!=this.username?""!=this.email&&null!=this.email?(e=await i({username:this.username,email:this.email}),0==e.code?(x(this,"初始密码已发送至您的邮箱，请注意查收"),this.changeValiCode(),this.pageType=1):x(this,e.msg)):x(this,"邮箱为空,请填写邮箱"):x(this,"账号为空,请填写账号")},async register(){let e=await l({validateCodeId:this.id,validateCode:this.valiCode});if(0!=e.code)return x(this,e.msg),void this.changeValiCode();""!=this.username&&null!=this.username?""!=this.password&&null!=this.password?""!=this.perName&&null!=this.perName?""!=this.email&&null!=this.email?""!=this.role&&null!=this.role?(e=await o({username:this.username,password:this.password,perName:this.perName,email:this.email,role:this.role}),0==e.code?(x(this,"已注册成功！"),this.changeValiCode(),this.pageType=1):x(this,e.msg)):x(this,"角色为空,请选择角色"):x(this,"邮箱为空,请填写邮箱"):x(this,"姓名为空,请填写姓名"):x(this,"账号为空,请填写密码"):x(this,"账号为空,请填写账号")},async loginSubmit(){const e=await l({validateCodeId:this.id,validateCode:this.valiCode});if(0!=e.code)return x(this,e.msg),void this.changeValiCode();if(""==this.username||null==this.username)x(this,"用户名为空");else if(""==this.password||null==this.password)x(this,"密码为空");else{const e=a();try{await e.login(this.username,this.password),await e.setNavi(),this.remember?e.saveAccount(J(this.username),J(this.password)):e.removeAccount(),t.push({path:"/MainPage"})}catch(s){x(this,"登录失败!")}}}}}),X={class:"page flex-col"},Y={class:"group1 flex-col"},$=f('<div class="section1 flex-col"><div class="main1 flex-row justify-between"><div class="mod1 flex-col"></div><span class="word1">欢迎来到教务管理系统！</span></div><div class="main2 flex-col"><div class="group2 flex-col"></div></div></div>',1),ee={class:"section2 flex-row"},se=d("div",{class:"box1 flex-col"},null,-1),ae={class:"box2 flex-col"},le={class:"wrap1 flex-row justify-between"},ie=d("div",{class:"group3 flex-col"},null,-1),oe=d("div",{class:"box3 flex-col"},[d("div",{class:"block1 flex-row justify-between"},[d("div",{class:"block2 flex-col"}),d("span",{class:"word3"},"微信登录")])],-1),te=d("div",{class:"section3 flex-col"},[d("span",{class:"txt1"},"copyright ©山东大学软件学院")],-1),re={class:"group4 flex-col"},ce={class:"group5 flex-col"},de={class:"section4 flex-col"},ne=d("div",{class:"box4 flex-col"},null,-1),pe={class:"box5 flex-col"},ue={key:0,class:"main3 flex-col"},he=d("div",{class:"main4 flex-col"},null,-1),me={class:"main5 flex-col"},fe={class:"wrap2 flex-row justify-between"},ve=d("div",{class:"main6 flex-col"},[d("div",{class:"box6 flex-col"})],-1),xe={class:"main7 flex-col"},ge={class:"wrap3 flex-row justify-between"},we=d("div",{class:"group6 flex-col"},[d("div",{class:"mod2 flex-col"})],-1),Ce={class:"main8 flex-col"},ye={class:"wrap4 flex-row"},be=d("div",{class:"bd1 flex-col"},[d("div",{class:"block3 flex-col"})],-1),Ae=["src"],Te={class:"ImageText1 flex-col"},ke={class:"group7 flex-row justify-between"},Ve=d("div",{class:"TextGroup1 flex-col"},[d("span",{class:"txt3"},"记住密码")],-1),Ue=[d("span",{class:"info1"},"登录",-1)],Be={key:1,class:"main3 flex-col"},Fe=d("span",{class:"callBackPass"},"初始密码",-1),Se={class:"mod33 flex-row"},je=d("span",{class:"word44"},"登录账号：",-1),Ee={class:"mod33 flex-row"},Ne=d("span",{class:"word44"},"电子邮箱：",-1),We={class:"mod33 flex-row"},De=d("span",{class:"word44"},"验证码：",-1),Ie=["src"],Pe={class:"ImageText19 flex-col"},ze={class:"outer49 flex-col justify-between"},Me=[d("span",{class:"info59"},"初始密码",-1)],Ze=[d("span",{class:"word89"},"返回登录",-1)],Ge={key:2,class:"main3 flex-col"},Le=d("span",{class:"callBackPass"},"用户注册",-1),Re={class:"modd33 flex-row"},_e=d("span",{class:"word44"},"账号：",-1),He={class:"modd33 flex-row"},qe=d("span",{class:"word44"},"姓名：",-1),Je={class:"modd33 flex-row"},Ke=d("span",{class:"word44"},"密码：",-1),Oe={class:"modd33 flex-row"},Qe=d("span",{class:"word44"},"邮箱：",-1),Xe={class:"modd33 flex-row"},Ye=d("span",{class:"word44"},"角色：",-1),$e=[d("option",{value:"ADMIN"},"管理员",-1),d("option",{value:"STUDENT"},"学生",-1),d("option",{value:"TEACHER"},"教师",-1)],es={class:"modd33 flex-row"},ss=d("span",{class:"word44"},"验证码：",-1),as=["src"],ls={class:"ImageText19 flex-col"},is={class:"outer49 flex-col justify-between"},os=[d("span",{class:"info59"},"注册提交",-1)],ts=[d("span",{class:"word89"},"返回登录",-1)],rs=d("div",{class:"box7 flex-col"},[d("span",{class:"txt4"},"建议使用谷歌浏览器chrome,windows自带浏览器Microsoft Edge,360浏览器请选用极速模式，打开微信小程序二维码，手机扫码直接登录微信小程序。")],-1);const cs=r(Q,[["render",function(e,s,a,l,i,o){return v(),c("div",X,[d("div",Y,[$,d("div",ee,[se,d("div",ae,[d("div",le,[ie,d("span",{class:"word2",onClick:s[0]||(s[0]=s=>e.pageRegister())},"新用户注册")])]),oe]),te]),d("div",re,[d("div",ce,[d("div",de,[ne,d("div",pe,[1==e.pageType?(v(),c("div",ue,[he,d("div",null,[d("div",me,[d("div",fe,[ve,n(d("input",{class:"inputWidth","onUpdate:modelValue":s[1]||(s[1]=s=>e.username=s),placeholder:"请输入账号"},null,512),[[p,e.username]])])]),d("div",xe,[d("div",ge,[we,n(d("input",{class:"inputWidth","onUpdate:modelValue":s[2]||(s[2]=s=>e.password=s),type:"password",placeholder:"请输入的密码"},null,512),[[p,e.password]])])]),d("div",Ce,[d("div",ye,[be,n(d("input",{class:"codeWidth","onUpdate:modelValue":s[3]||(s[3]=s=>e.valiCode=s),placeholder:"请输入的验证码"},null,512),[[p,e.valiCode]]),d("img",{onClick:s[4]||(s[4]=s=>e.changeValiCode()),class:"img1",referrerpolicy:"no-referrer",src:e.img},null,8,Ae)])]),d("div",Te,[d("div",ke,[n(d("input",{type:"checkbox","onUpdate:modelValue":s[5]||(s[5]=s=>e.remember=s)},null,512),[[u,e.remember]]),Ve])]),d("div",{class:"main9 flex-col",onClick:s[6]||(s[6]=s=>e.loginSubmit())},Ue)]),d("span",{onClick:s[7]||(s[7]=s=>e.forgetPass()),class:"info2"},"忘记密码")])):h("",!0),2==e.pageType?(v(),c("div",Be,[Fe,d("div",Se,[je,n(d("input",{class:"inputWidth2","onUpdate:modelValue":s[8]||(s[8]=s=>e.username=s),placeholder:"填写教师号/学号"},null,512),[[p,e.username]])]),d("div",Ee,[Ne,n(d("input",{class:"inputWidth2","onUpdate:modelValue":s[9]||(s[9]=s=>e.email=s),placeholder:"请输入的邮箱"},null,512),[[p,e.email]])]),d("div",We,[De,n(d("input",{class:"inputWidth3","onUpdate:modelValue":s[10]||(s[10]=s=>e.valiCode=s),placeholder:"请输入验证码"},null,512),[[p,e.valiCode]]),d("img",{onClick:s[11]||(s[11]=s=>e.changeValiCode()),class:"img2",referrerpolicy:"no-referrer",src:e.img},null,8,Ie)]),d("div",Pe,[d("div",ze,[d("div",{class:"box19 flex-col",onClick:s[12]||(s[12]=s=>e.initPassword())},Me),d("div",{class:"TextGroup19 flex-col",onClick:s[13]||(s[13]=s=>e.backLogin())},Ze)])])])):h("",!0),3==e.pageType?(v(),c("div",Ge,[Le,d("div",Re,[_e,n(d("input",{class:"inputWidth2","onUpdate:modelValue":s[14]||(s[14]=s=>e.username=s),placeholder:"填写账号"},null,512),[[p,e.username]])]),d("div",He,[qe,n(d("input",{class:"inputWidth2","onUpdate:modelValue":s[15]||(s[15]=s=>e.perName=s),placeholder:"请输入的姓名"},null,512),[[p,e.perName]])]),d("div",Je,[Ke,n(d("input",{class:"inputWidth2","onUpdate:modelValue":s[16]||(s[16]=s=>e.password=s),type:"password"},null,512),[[p,e.password]])]),d("div",Oe,[Qe,n(d("input",{class:"inputWidth2","onUpdate:modelValue":s[17]||(s[17]=s=>e.email=s),placeholder:"请输入的邮箱"},null,512),[[p,e.email]])]),d("div",Xe,[Ye,n(d("select",{class:"inputWidth2","onUpdate:modelValue":s[18]||(s[18]=s=>e.role=s)},$e,512),[[m,e.role]])]),d("div",es,[ss,n(d("input",{class:"inputWidth3","onUpdate:modelValue":s[19]||(s[19]=s=>e.valiCode=s),placeholder:"请输入验证码"},null,512),[[p,e.valiCode]]),d("img",{onClick:s[20]||(s[20]=s=>e.changeValiCode()),class:"img2",referrerpolicy:"no-referrer",src:e.img},null,8,as)]),d("div",ls,[d("div",is,[d("div",{class:"box19 flex-col",onClick:s[21]||(s[21]=s=>e.register())},os),d("div",{class:"TextGroup19 flex-col",onClick:s[22]||(s[22]=s=>e.backLogin())},ts)])])])):h("",!0)]),rs])])])])}]]);export{cs as default};
