var inTime,outTime,stayTime;
inTime=new Date().getTime();
    
window.onbeforeunload=function(){
    outTime=new Date().getTime();
    stayTime=(outTime-inTime)/1000;
    //console.log(stayTime);
    $.ajax({
    	type:'POST',
    	url:'/stayTime/logBrowseTime',
    	data:{stayTime:stayTime},
    	success:function(){
    	   }
    });
};