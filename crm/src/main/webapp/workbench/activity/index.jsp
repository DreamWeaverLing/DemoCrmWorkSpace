<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";
%>
<!DOCTYPE html>
<html>
<head>
<base href="<%=basePath%>">
<meta charset="UTF-8">

<link href="jquery/bootstrap_3.3.0/css/bootstrap.min.css" type="text/css" rel="stylesheet" />
<link href="jquery/bootstrap-datetimepicker-master/css/bootstrap-datetimepicker.min.css" type="text/css" rel="stylesheet" />

<script type="text/javascript" src="jquery/jquery-1.11.1-min.js"></script>
<script type="text/javascript" src="jquery/bootstrap_3.3.0/js/bootstrap.min.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/js/bootstrap-datetimepicker.js"></script>
<script type="text/javascript" src="jquery/bootstrap-datetimepicker-master/locale/bootstrap-datetimepicker.zh-CN.js"></script>

<link rel="stylesheet" type="text/css" href="jquery/bs_pagination/jquery.bs_pagination.min.css" />
<script type="text/javascript" src="jquery/bs_pagination/jquery.bs_pagination.min.js"></script>
<script type="text/javascript" src="jquery/bs_pagination/en.js"></script>
<script>
function pageList(pageNo, pageSize) {
	$("#search-name").val($.trim($("#hidden-name").val()));
	$("#search-owner").val($.trim($("#hidden-owner").val()));
	$("#search-startTime").val($.trim($("#hidden-startTime").val()));
	$("#search-endTime").val($.trim($("#hidden-endTime").val()));

	$.ajax({
		dataType: "json",
		url: "workbench/activity/search.do",
		type: "get",
		data: {
			"pageNo": pageNo,
			"pageSize": pageSize,
			"Name": $.trim($("#search-name").val()),
			"Owner": $.trim($("#search-owner").val()),
			"startTime": $.trim($("#search-startTime").val()),
			"endTime": $.trim($("#search-endTime").val())
		},
		success: function (data) {
			var html = ""
			$.each(data.dataList, function (i, n) {
				html += "<tr class=\"active\">"
				html += "	<td><input type=\"checkbox\" name='selectActivity' value=" + n.id + "></td>"
				html += "   <td><a style=\"text-decoration: none; cursor: pointer;\" " +
						"onclick=\"window.location.href='workbench/activity/detail.do?id=" + n.id + "';\">" + n.name + "</a></td>"
				html += "	<td>" + n.owner + "</td>"
				html += "	<td>" + n.startDate + "</td>"
				html += "	<td>" + n.endDate + "</td>"
				html += "</tr>"

			})
			$("#activityBody").html(html)
			var totalPages = data.total % pageSize == 0 ? (data.total / pageSize) : (parseInt(data.total / pageSize) + 1)
			$("#activityPage").bs_pagination({
				currentPage: pageNo, // 页码
				rowsPerPage: pageSize, // 每页显示的记录条数
				maxRowsPerPage: 20, // 每页最多显示的记录条数
				totalPages: totalPages, // 总页数
				totalRows: data.total, // 总记录条数

				visiblePageLinks: 5, // 显示几个卡片

				showGoToPage: true,
				showRowsPerPage: true,
				showRowsInfo: true,
				showRowsDefaultInfo: true,

				onChangePage: function (event, data) {
					pageList(data.currentPage, data.rowsPerPage);
				}
			});

		}
	})
}

$(function () {
	// 使用日历插件
	$(".time").datetimepicker({
		minView: "month",
		language: 'zh-CN',
		format: 'yyyy-mm-dd',
		autoclose: true,
		todayBtn: true,
		pickerPosition: "bottom-left",

	});
	// 禁用用户手动输入日期
	$(".time").keypress(function () {
		event.preventDefault();
	})

	// 创建市场活动
	$("#createActivityBtn").click(function () {


		// 创建市场活动模态窗口
		$("#createActivityModal").modal("show");
		$.ajax({
			dataType: "json",
			url: "workbench/activity/getUserList.do",
			type: "get",
			success: function (data) {
				var html = ""
				$.each(data, function (i, n) {
					html += "<option value=" + n.id + ">" + n.name + "</option>"
				})
				$("#create-marketActivityOwner").html(html)
				$("#create-marketActivityOwner").val("${user.id}")
			}
		})
	})

	// 保存按钮
	$("#save-btn").click(function () {
		$.ajax({
			dataType: "json",
			url: "workbench/activity/save.do",
			type: "post",
			data: {
				"Owner": $.trim($("#create-marketActivityOwner").val()),
				"Name": $.trim($("#create-marketActivityName").val()),
				"startTime": $.trim($("#create-startTime").val()),
				"endTime": $.trim($("#create-endTime").val()),
				"cost": $.trim($("#create-cost").val()),
				"description": $.trim($("#create-describe").val()),
			},
			success: function (data) {
				if (data.success) {
					$("#createActivityModal").modal("hide");
					$("#createActivityForm")[0].reset();
					pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
					alert("保存成功！")

				} else {
					alert("保存失败！")
				}
			}
		})
	})

	// 打开页面时自动刷新列表
	pageList(1, 2);

	// 查询功能
	$("#search-btn").click(function () {
		//alert("a sf")
		$("#hidden-name").val($.trim($("#search-name").val()));
		$("#hidden-owner").val($.trim($("#search-owner").val()));
		$("#hidden-startTime").val($.trim($("#search-startTime").val()));
		$("#hidden-endTime").val($.trim($("#search-endTime").val()));

		pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
	});

	// 全选功能
	$("#selectAll").click(function () {
		$("input[name='selectActivity']").prop("checked", this.checked);
	})

	// 单选功能
	$("#activityBody").on("click", "input[name='selectActivity']", function () {
		$("#selectAll").prop("checked", $("input[name='selectActivity']:checked").length == $("input[name='selectActivity']").length)
	})

	// 删除功能
	$("#deleteActivityBtn").click(function () {
		var $checked = $("input[name='selectActivity']:checked")
		if ($checked.length == 0) {
			alert("请选择一个市场活动！")
		} else {
			if (window.confirm("确定删除吗？")) {
				var param = ""
				$.each($checked, function (i, n) {
					param += "id=" + n.value;
					if (i < $checked.length - 1) {
						param += "&"
					}
				})
				$.ajax({
					dataType: "json",
					url: "workbench/activity/delete.do",
					data: param,
					type: "post",
					success: function (data) {
						var num = data.num;
						if (num > 0) {
							// alert("删除成功，已删除"+num+"条记录！")
							pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
							$("#selectAll").prop("checked", false)
						} else if (num == 0) {
							alert("删除失败，请重新尝试！")
							pageList(1, $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
						}
					}
				})
			}
		}
	})

	// 修改功能
	$("#editActivityBtn").click(function () {
		var $checked = $("input[name='selectActivity']:checked")
		if ($checked.length == 0 || $checked.length > 1) {
			alert("请选择一个市场活动！")
		} else {
			var id = $checked.val()
			$("#editActivityModal").modal("show");
			$.ajax({
				dataType: "json",
				url: "workbench/activity/getUListAndActivityDetail.do",
				type: "get",
				data: {"id": id},
				success: function (data) {
					var html = ""
					$.each(data.uList, function (i, n) {
						html += "<option value=" + n.id + ">" + n.name + "</option>"
					})
					$("#edit-marketActivityOwner").html(html)

					$("#edit-marketActivityId").val(data.activityDetail.id)
					$("#edit-marketActivityOwner").val(data.activityDetail.owner)
					$("#edit-marketActivityName").val(data.activityDetail.name);
					$("#edit-startTime").val(data.activityDetail.startDate);
					$("#edit-endTime").val(data.activityDetail.endDate);
					$("#edit-cost").val(data.activityDetail.cost);
					$("#edit-describe").val(data.activityDetail.description);
				}
			})
		}
	})

	// 更新功能
	$("#editActivity").click(function () {
		$.ajax({
			dataType: "json",
			url: "workbench/activity/edit.do",
			type: "post",
			data: {
				"id": $.trim($("#edit-marketActivityId").val()),
				"owner": $.trim($("#edit-marketActivityOwner").val()),
				"name": $.trim($("#edit-marketActivityName").val()),
				"startDate": $.trim($("#edit-startTime").val()),
				"endDate": $.trim($("#edit-endTime").val()),
				"cost": $.trim($("#edit-cost").val()),
				"description": $.trim($("#edit-describe").val()),
			},
			success: function (data) {
				if (data.success) {
					$("#editActivityModal").modal("hide");
					$("#editActivityForm")[0].reset();
					pageList($("#activityPage").bs_pagination('getOption', 'currentPage'), $("#activityPage").bs_pagination('getOption', 'rowsPerPage'));
					alert("更新成功！")

				} else {
					alert("更新失败！")
				}
			}
		})
	})
});

</script>

</head>
<body>
	<input type="hidden" id="hidden-name">
	<input type="hidden" id="hidden-owner">
	<input type="hidden" id="hidden-startTime">
	<input type="hidden" id="hidden-endTime">

	<!-- 创建市场活动的模态窗口 -->
	<div class="modal fade" id="createActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel1">创建市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="createActivityForm">
					
						<div class="form-group">
							<label for="create-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="create-marketActivityOwner">
								</select>
							</div>
                            <label for="create-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-marketActivityName">
                            </div>
						</div>
						
						<div class="form-group">
							<label for="create-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-startTime">
							</div>
							<label for="create-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="create-endTime">
							</div>
						</div>
                        <div class="form-group">

                            <label for="create-cost" class="col-sm-2 control-label">成本</label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="create-cost">
                            </div>
                        </div>
						<div class="form-group">
							<label for="create-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="create-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="save-btn">保存</button>
				</div>
			</div>
		</div>
	</div>
	
	<!-- 修改市场活动的模态窗口 -->
	<div class="modal fade" id="editActivityModal" role="dialog">
		<div class="modal-dialog" role="document" style="width: 85%;">
			<div class="modal-content">
				<div class="modal-header">
					<button type="button" class="close" data-dismiss="modal">
						<span aria-hidden="true">×</span>
					</button>
					<h4 class="modal-title" id="myModalLabel2">修改市场活动</h4>
				</div>
				<div class="modal-body">
				
					<form class="form-horizontal" role="form" id="editActivityForm">

						<input type="hidden" id="edit-marketActivityId">
						<div class="form-group">
							<label for="edit-marketActivityOwner" class="col-sm-2 control-label">所有者<span style="font-size: 15px; color: red;">*</span></label>
							<div class="col-sm-10" style="width: 300px;">
								<select class="form-control" id="edit-marketActivityOwner">
<%--								  <option>zhangsan</option>
								  <option>lisi</option>
								  <option>wangwu</option>--%>
								</select>
							</div>
                            <label for="edit-marketActivityName" class="col-sm-2 control-label">名称<span style="font-size: 15px; color: red;">*</span></label>
                            <div class="col-sm-10" style="width: 300px;">
                                <input type="text" class="form-control" id="edit-marketActivityName">
                            </div>
						</div>

						<div class="form-group">
							<label for="edit-startTime" class="col-sm-2 control-label">开始日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-startTime">
							</div>
							<label for="edit-endTime" class="col-sm-2 control-label">结束日期</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control time" id="edit-endTime">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-cost" class="col-sm-2 control-label">成本</label>
							<div class="col-sm-10" style="width: 300px;">
								<input type="text" class="form-control" id="edit-cost">
							</div>
						</div>
						
						<div class="form-group">
							<label for="edit-describe" class="col-sm-2 control-label">描述</label>
							<div class="col-sm-10" style="width: 81%;">
								<textarea class="form-control" rows="3" id="edit-describe"></textarea>
							</div>
						</div>
						
					</form>
					
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>
					<button type="button" class="btn btn-primary" data-dismiss="modal" id="editActivity">更新</button>
				</div>
			</div>
		</div>
	</div>
	
	
	
	
	<div>
		<div style="position: relative; left: 10px; top: -10px;">
			<div class="page-header">
				<h3>市场活动列表</h3>
			</div>
		</div>
	</div>
	<div style="position: relative; top: -20px; left: 0px; width: 100%; height: 100%;">
		<div style="width: 100%; position: absolute;top: 5px; left: 10px;">
		
			<div class="btn-toolbar" role="toolbar" style="height: 80px;">
				<form class="form-inline" role="form" style="position: relative;top: 8%; left: 5px;">
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">名称</div>
				      <input class="form-control" type="text" id="search-name">
				    </div>
				  </div>
				  
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">所有者</div>
				      <input class="form-control" type="text" id="search-owner">
				    </div>
				  </div>


				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">开始日期</div>
					  <input class="form-control time" type="text" id="search-startTime"/>
				    </div>
				  </div>
				  <div class="form-group">
				    <div class="input-group">
				      <div class="input-group-addon">结束日期</div>
					  <input class="form-control time" type="text" id="search-endTime"/>
				    </div>
				  </div>
				  
				  <button type="button" id="search-btn" class="btn btn-default">查询</button>
				  
				</form>
			</div>
			<div class="btn-toolbar" role="toolbar" style="background-color: #F7F7F7; height: 50px; position: relative;top: 5px;">
				<div class="btn-group" style="position: relative; top: 18%;">
				  <button type="button" class="btn btn-primary" id="createActivityBtn"><span class="glyphicon glyphicon-plus"></span> 创建</button>
				  <button type="button" class="btn btn-default" id="editActivityBtn"><span class="glyphicon glyphicon-pencil"></span> 修改</button>
				  <button type="button" class="btn btn-danger" id="deleteActivityBtn"><span class="glyphicon glyphicon-minus"></span> 删除</button>
				</div>
				
			</div>
			<div style="position: relative;top: 10px;">
				<table class="table table-hover">
					<thead>
						<tr style="color: #B3B3B3;">
							<td><input type="checkbox" id="selectAll"/></td>
							<td>名称</td>
                            <td>所有者</td>
							<td>开始日期</td>
							<td>结束日期</td>
						</tr>
					</thead>
					<tbody id="activityBody">
<%--						<tr class="active">
							<td><input type="checkbox" /></td>
							<td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
							<td>2020-10-10</td>
							<td>2020-10-20</td>
						</tr>
                        <tr class="active">
                            <td><input type="checkbox" /></td>
                            <td><a style="text-decoration: none; cursor: pointer;" onclick="window.location.href='detail.jsp';">发传单</a></td>
                            <td>zhangsan</td>
                            <td>2020-10-10</td>
                            <td>2020-10-20</td>
                        </tr>--%>
					</tbody>
				</table>
			</div>

			<div style="height: 50px; position: relative;top: 30px;">

				<div id="activityPage"></div>

			</div>
			
		</div>
		
	</div>
</body>
</html>