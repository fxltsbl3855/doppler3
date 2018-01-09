$(document).ready(function(){
	
	$.post($.modulePath + "/app/dashboard.shtml",function(data){
		
		var appNum = data.appNum;
		var moduleNum = data.moduleNum;
		var appReqNum = data.appReqNum;
		var errorNum = data.errorNum;
		
		var appReqList = data.appReqList;
		var moduleReqList = data.moduleReqList;
		var errorList = data.errorList;
		var errorStat = data.errorStat;
		
		$("#appNum").html(appNum);
		$("#moduleNum").html(moduleNum);
		$("#appReqNum").html(appReqNum);
		$("#errorNum").html(errorNum);
		
		var appChart = echarts.init(document.getElementById('appChart'));
		var moduleChart = echarts.init(document.getElementById('moduleChart'));
		var errorChart = echarts.init(document.getElementById('errorChart'));
		var errorStatChart = echarts.init(document.getElementById('errorStatChart'));
		
		// 系统请求量动态加载数据
		var appLegendDatas = new Array();	
		var appSeriesDatas = new Array();
		for (var i in appReqList){
			appLegendDatas.push(appReqList[i].name);
			var appSeriesData = new Object();
			appSeriesData.value = appReqList[i].num;
			appSeriesData.name =appReqList[i].name;
			appSeriesDatas.push(appSeriesData);
		}
		
		// 模块请求量动态加载数据
		var moduleLegendDatas = new Array();	
		var moduleSeriesDatas = new Array();
		for (var i in moduleReqList){
			moduleLegendDatas.push(moduleReqList[i].name);
			var moduleSeriesData = new Object();
			moduleSeriesData.value = moduleReqList[i].num;
			moduleSeriesData.name =moduleReqList[i].name;
			moduleSeriesDatas.push(moduleSeriesData);
		}
		
		// 异常曲线动态加载数据
		var errorLineLegendDatas = new Array();	
		var errorLineSeriesDatas = new Array();
		for (var i in errorList){
			errorLineLegendDatas.push(errorList[i].dateStr);
			errorLineSeriesDatas.push(errorList[i].num == 0 ? '0' : errorList[i].num);
		}
		
		//异常分类饼图动态加载数据
		var errorLegendDatas = new Array();	
		var errorSeriesDatas = new Array();
		for (var i in errorStat){
			errorLegendDatas.push(errorStat[i].name);
			var errorSeriesData = new Object();
			errorSeriesData.value = errorStat[i].num;
			errorSeriesData.name =errorStat[i].name;
			errorSeriesDatas.push(errorSeriesData);
		}
		
		//系统量options
		var appOptions = {
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    series : [
		        {
		            name: '访问来源',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            data:appSeriesDatas,
		            itemStyle : {
		                normal : {
		                    label : {
		                        show :true,
		                        position:'top',
		                        formatter: "{b}:\n{c}",
		                        textStyle : {
		                        color : 'blue',
		                        fontSize : 12,
		                        fontWeight : 'bold'
		                        }
		                    },

		                }
		            }
		        }
		    ]
		};
		//模块量options
		var moduleOptions = {
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    series : [
		        {
		            name: '访问来源',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            data: moduleSeriesDatas,
		            itemStyle : {
		                normal : {
		                    label : {
		                        show :true,
		                        position:'top',
		                        formatter: "{b}:\n{c}",
		                        textStyle : {
		                        color : 'blue',
		                        fontSize : 12,
		                        fontWeight : 'bold'
		                        }
		                    },

		                }
		            }
		        }
		    ]
		};
		//异常曲线 options
		var errorOptions = {
			tooltip : {
		        trigger: 'axis'
		    },
		    legend: {
		    },
		    calculable : true,
		    xAxis : [
		        {
		            type : 'category',
		            boundaryGap : false,
		            data : errorLineLegendDatas
		        }
		    ],
		    yAxis : [
		        {
		            type : 'value'
		        }
		    ],
		    series : [
		        {
		            name:'异常数',
		            type:'line',
		            data : errorLineSeriesDatas
		        }
		    ]
		};
		
		//昨日异常分类options
		var errorStatOptions = {
		    tooltip : {
		        trigger: 'item',
		        formatter: "{a} <br/>{b} : {c} ({d}%)"
		    },
		    series : [
		        {
		            name: '访问来源',
		            type: 'pie',
		            radius : '55%',
		            center: ['50%', '60%'],
		            data:errorSeriesDatas,
		            itemStyle : {
		                normal : {
		                    label : {
		                        show :true,
		                        position:'top',
		                        formatter: "{b}:\n{c}",
		                        textStyle : {
		                        color : 'blue',
		                        fontSize : 12,
		                        fontWeight : 'bold'
		                        }
		                    },

		                }
		            }
		        }
		    ]
		};
		
		appChart.setOption(appOptions);
		moduleChart.setOption(moduleOptions);
		errorChart.setOption(errorOptions);
		errorStatChart.setOption(errorStatOptions);
	});
});


var Index = function(){
	return {
		forward : function(url) {
			if(url == "dashboard.shtml"){
				$('#dashboard')[0].style.display = "block";
				$('#app')[0].style.display = "none";
				$('#exceptionMonitor')[0].style.display = "none";
			}
			if(url == "app.shtml"){
				$('#dashboard')[0].style.display = "none";
				$('#app')[0].style.display = "block";
				$('#exceptionMonitor')[0].style.display = "none";
			}
			if(url == "exceptionMonitor.shtml"){
				$('#dashboard')[0].style.display = "none";
				$('#app')[0].style.display = "none";
				$('#exceptionMonitor')[0].style.display = "block";
			}
		}
	};
}();
