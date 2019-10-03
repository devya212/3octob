<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<script src="https://code.jquery.com/jquery-1.12.4.min.js"></script>
<link rel="stylesheet" type="text/css" href="<c:url value='/resources/StaticFiles/Font-Awesome/css/font-awesome.min.css'/>">
<script type="text/javascript">
$(document).ready(function() {
    // Configure/customize these variables.
    var showChar = 200;  // How many characters are shown by default
    var ellipsestext = "...";
    var moretext = "Show more";
    var lesstext = "Show less";
    

    $('.more').each(function() {
        var content = $(this).html();
 
        if(content.length > showChar) {
 
            var c = content.substr(0, showChar);
            var h = content.substr(showChar, content.length - showChar);
 
            var html = c + '<span class="moreellipses">' + ellipsestext+ '</span><span class="morecontent" style="float: left;"><span>' + h + '</span><a href="" class="morelink">' + moretext + '</a></span>';
 
            $(this).html(html);
        }
 
    });
 
    $(".morelink").click(function(){
        if($(this).hasClass("less")) {
            $(this).removeClass("less");
            $(this).html(moretext);
        } else {
            $(this).addClass("less");
            $(this).html(lesstext);
        }
        $(this).parent().prev().toggle();
        $(this).prev().toggle();
        return false;
    });
});
</script>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>News Display</title>

<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0-alpha.3/css/bootstrap.min.css"
	integrity="sha384-MIwDKRSSImVFAZCVLtU0LMDdON6KVCrZHyVQQj6e8wIEJkW4tvwqXrbMIya1vriY"
	crossorigin="anonymous">
<link href="css/custom.css" rel="stylesheet">

<link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css">
<link href="<c:url value="/resources/Display.css" />" rel="stylesheet">
<style>
.shadow {
width:auto;height:auto;
border: solid 1px #555;
 border-radius: 15px  15px 15px 15px;	
  
 
}
#rcorners1 {
    border-radius: 25px;
    padding: 20px; 
    width: 200px;
    height: 150px;    
}
.morecontent span {
    display: none;
}
.morelink {
    display: block;
}
</style>
</head>
<body> 
<script type="text/javascript" src="http://code.jquery.com/jquery-1.10.1.min.js"></script>
    <script type="text/javascript">

function views(id1)
{
	$.ajax({
	    type : "GET",
	    url : "checkviews",
	    data : {
	        "id" : id1
	    },
	    success: function(response){
	        /* $('#noofviews').val(response); */
	        
	        /* $('#noofviews').html(""); 
	        $('#noofviews').html(response);
	    	 *//* location.reload(); */
	    	/* alert(response); */
	         $('#view_'+id1).text(""); 
	        $('#view_'+id1).text(response);
	   
	    }
	});
}
</script>
<script>
function likes(id2)
{
	$.ajax({
	    type : "GET",
	    url : "checklikes",
	    data : {
	        "id" : id2
	    },
	    success: function(response){
	        /* $('#noofviews').val(response); */
	        
	     /*    $('#noofviews').html(response); */
	     /* location.reload(); */
	    $('#like_'+id2).text(""); 
	        $('#like_'+id2).text(response);
	    	
	     $('#likes_'+id2).css('background-color','blue'); 
	    /* alert(response); */
	    }
	});
}

</script>

<%-- $.ajax({
    type : "GET",
    url : "${pageContext.request.contextPath}/check",
    data : {
        "id" : ${articleCount}
    },
    success: function(data){
        $('#input_field').val(data);
    }
});
 --%>
 <div class="col-md-6" style="float: right;width=30%;">
								<div class="input-group">
									<input type="text" id="searchBar" name="searchString" placeholder="Search" class="form-control">
									<span class="input-group-addon" style="cursor: pointer;" id="searchIcon"><span class="glyphicon glyphicon-search"></span></span>
								</div>
							</div>
						</div>
		<script>
	$("#searchIcon").click(function () {
	searchDiscussionThreadsFunction();
		});
		
   $("#searchBar").keypress(function(e) {
		  if(e.which == 13) {
		  	$("#searchIcon").click();
		  }
       });
   function searchDiscussionThreadsFunction()
   {
	   $.ajax({
		    type : "POST",
		    url : "searchArticles",
		    data:{
		    	
		    	"searchString" : $("#searchBar").val()
		    }
		    success: function(response){
		    	alert("hi");
		    /*  $('#table1').html(response); */
		    }
		});
   }
   
   
   
   <%-- function searchDiscussionThreadsFunction()
	{
		console.log("md");
		var loadingIcon = '<img alt="Loading" style="width:150px;" src="<c:url value='/resources/StaticFiles/images/Loading_icon.gif'/>">';
		var buttonContent = $('.load-more-button').html();
		$('.load-more-button').html(loadingIcon);
		$('.discussion-list-discussions').html('');
		$("#currentPage").val(1);
		buttonContent = '';
		processing = true;
		
		var searchedString = $("#searchBar").val()
		
		$.post('<%= request.getContextPath() %>/forum/searchDiscussionsThreads',
		{
			searchString : $("#searchBar").val(),
			CSRFToken : "${sessionScope.CSRFToken_tokenval}"
		},
		function(data, status) {
			$("#currentPage").val(parseInt($("#currentPage").val()));
			$('.load-more-button').html(buttonContent);
               
           var result = data.replace(/\s/g,'');
 			if(status === 'success')
 			{
               $('.discussion-list-discussions').html('<span style="font-weight: bold;">You searched for: '+searchedString+'</span>');
               $('.discussion-list-discussions').append(data);
               $("#searchBar").val('');
 				if(result === "")
 				{
 					$('.load-more-button').html('<h4 style="font-weight: bold;">No Discussion Thread found!!</h4>');
 					processing = true;
 				}
 				else
 				{
 					$('.load-more-button').html(buttonContent);
 				}
 			}
		});
	}
	
 --%>
</script>
       <table id="table1">
			<thead>
				
			<c:forEach items="${articleList}" var="article">
	<div class="row" style="margin-left:10px;">
		
				<article class="col-xs-12">
				<div class="shadow">
					<div class="media">
						<div class="media-left">
						
							<%-- <a href="#">
								<img src="data:image/jpg;base64,${newsList.newsImage}" width="200" height="120" id="rcorners1"/>
							</a> --%>
						</div>
						<div class="media-body">
							<h2 class="media-heading" style="font-size:20px;" ><strong><a style="color: rgba(0, 0, 0, 0.9);" href="#"><c:out value="${article.articleTitle}" /></a></strong></h2>
							<h><c:out value="Updated on:${article.upload_date}"/></h> 
							<br><br>
							 <p style="font-size:14px;">
							 	<span class="more">
									<c:out value="${article.articleDescription}" />
								</span>
								<c:if test="${article.file_data != null}">
									<span style="float: right; margin-right: 30px;">
										<a href="${pageContext.request.contextPath}/showPdfArticle/${article.upload_id}" style="font-size:14px; " target="_blank" onclick="return views('${article.upload_id}')"><i class="fa fa-file-pdf-o" style="font-size:14px;color:red"></i>Preview Pdf</a>
									</span>
								</c:if>
							</p>
							<span class="glyphicon glyphicon-eye-open"></span>&nbsp;&nbsp;<span id="view_${article.upload_id}">${article.usersWhoHaveViewedThisThread.size()}</span>&nbsp;&nbsp;<button onclick="return likes('${article.upload_id}')" id="likes_${article.upload_id}"><span class="glyphicon glyphicon-thumbs-up" aria-selected="true"></span></button>&nbsp;&nbsp;<span id="like_${article.upload_id}">${article.usersWhoHaveLikedThisThread.size()}</span>
						</div>
						
						</div>
					</div>
					<br>
				</article>
			</div> 
			</c:forEach>
				
				
			</thead>
		</table>

</body>
</html>