package com.nucleus.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.commons.CommonsMultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.nucleus.dao.UserDao;
import com.nucleus.functions.FileValidator;
import com.nucleus.pojo.DiscussionThread;
import com.nucleus.pojo.PFinnContributeArticle;
import com.nucleus.pojo.PFinnNewUser;
import com.nucleus.service.ArticleService;
import com.nucleus.service.UserService;
import com.nucleus.utility.DiscussionForumThreadListView;
import com.nucleus.utility.SearchParameterOperation;

/**
 * @author Vasu Sharma
 * @since 20 September 2018
 */

// Contribute article in case of User
@Controller
public class ArticleController 
{
	static final String PDFMEDIATYPE = "application/pdf";
	
	@Autowired
	private ArticleService articleService;

	@Autowired
	private UserService userService;

	@Autowired
	private UserDao userDao;

	/**
	 * @author Ajita Mittal
	 * @return list of all the articles
	 * This function will show all the articles
	 */	
	@RequestMapping(value = "/viewAllArticles")
	
	public ModelAndView allArticles() 
	{
		return new ModelAndView("DisplayArticles", "articleList", articleService.getAllApprovedArticles());
	}
	
@RequestMapping(value = "/checkviews")

public @ResponseBody String checkArticleviews(@RequestParam("id") int uploadId) 
	{
	System.out.println(uploadId);
	ModelMap model=new ModelMap();
	
	PFinnContributeArticle pfinnContributeArticle = articleService.get(uploadId);
	if(pfinnContributeArticle != null)
	{
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) 
			{
				PFinnNewUser user = userService.getUserByUserName(authentication.getName());
				articleService.checkIfUserHasViewedThisArticle(user, pfinnContributeArticle);
			}
	}
	
	PFinnContributeArticle pfinnContributeArticle1 = articleService.get(uploadId);
 /*model.addAttribute("viewcount",pfinnContributeArticle.getUsersWhoHaveViewedThisThread().size());*/
 /*System.out.println(pfinnContributeArticle1.getUsersWhoHaveViewedThisThread().size());*/
		return String.valueOf(pfinnContributeArticle1.getUsersWhoHaveViewedThisThread().size());
	}
	
	
@RequestMapping(value = "/checklikes")

public @ResponseBody String checkArticlelikes(@RequestParam("id") int uploadId) 
	{
	System.out.println(uploadId);
	ModelMap model=new ModelMap();
	
	PFinnContributeArticle pfinnContributeArticle = articleService.get(uploadId);
	if(pfinnContributeArticle != null)
	{
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) 
			{
				PFinnNewUser user = userService.getUserByUserName(authentication.getName());
				articleService.checkIfUserHasLikedThisArticle(user, pfinnContributeArticle);
			}
	}
	
	PFinnContributeArticle pfinnContributeArticle1 = articleService.get(uploadId);
 /*model.addAttribute("viewcount",pfinnContributeArticle.getUsersWhoHaveViewedThisThread().size());*/
 /*System.out.println(pfinnContributeArticle1.getUsersWhoHaveLikedThisThread().size());*/
		return String.valueOf(pfinnContributeArticle1.getUsersWhoHaveLikedThisThread().size());
	}


@RequestMapping(value="/searchArticles", method=RequestMethod.POST)
public ModelAndView searchArticles(@RequestParam("searchString") String searchString)
{
	ModelAndView modelAndView = new ModelAndView("DisplayArticles");
	if(searchString != null && !searchString.equalsIgnoreCase(""))
	{
		List<PFinnContributeArticle> pfinnContributeArticleList = articleService.searchArticles(searchString);

		
		modelAndView.addObject("articleList", pfinnContributeArticleList);
	}
	return modelAndView;
}







	/**
	 * @author Vasu Sharma
	 * @since 20 September 2018
	 */
	// display the form where file can be uploaded from system
	@RequestMapping("/contributeArticleForm")
	@PreAuthorize("hasRole('ROLE_USER')")
	public String contributeArticleForm() {
		return "contributeArticleForm";
	}

	/**
	 * @author Vasu Sharma
	 * @since 20 September 2018
	 */

	// after uploading save the document in database
	@RequestMapping(value = "/doUploadContributeArticle", method = RequestMethod.POST)
	@PreAuthorize("hasRole('ROLE_USER')")
	public ModelAndView doUploadContributeArticle(@RequestParam CommonsMultipartFile fileUpload, HttpServletRequest request) throws Exception {
		PFinnContributeArticle uploadFile = new PFinnContributeArticle();
		System.out.println("Saving file: " + fileUpload.getOriginalFilename());
	
		
		//source code of tika api
		InputStream is = fileUpload.getInputStream();
		FileValidator fileValidator=new FileValidator();
		String mediatype1=fileValidator.fileValidator(is,fileUpload);
		System.out.println("type of the file is"+mediatype1);
		 
			 
	    
	
		if (fileUpload != null) {
			  if(mediatype1.equalsIgnoreCase(PDFMEDIATYPE))
		  {
			// get user who is uploading
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken))
				uploadFile.setpFinnNewUser(userDao.getUserByUserName(authentication.getName()));

			// file data will be mapped to blob in database
			uploadFile.setFile_data(fileUpload.getBytes());

			// set current date of upload
			uploadFile.setUpload_date(new Date(System.currentTimeMillis()));

			// save method in service
			articleService.save(uploadFile);
		}

			  else
				{
					ModelAndView modelAndView=new ModelAndView("alert");
					modelAndView.addObject("alertMessage","please select a  pdf file");
					modelAndView.addObject("location","document.location.href = '"+request.getContextPath()+"/contributeArticleForm',true;");
					return modelAndView;
				}
		 
		}
	  return new ModelAndView("contributeArticleSuccess");
	}

	/**
	 * @author Vasu Sharma
	 * @since 20 September 2018
	 * This function will show article pdf
	 */
	// fetch document from database
	@RequestMapping("/getBlob")
	@PreAuthorize("hasRole('ROLE_USER')")
	public void getBlob( HttpServletResponse response) {
		// static call
		PFinnContributeArticle blobPdf = articleService.get(213);
		try {
			streamReport(response, blobPdf.getFile_data(), "abc.pdf");
		} catch (IOException e) 
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * @author Mukesh Dewangan
	 * @since 30 October 2018
	 * This function will return response as pdf
	 */
	protected void streamReport(HttpServletResponse response, byte[] data, String name)
            throws IOException {
        response.setContentType(PDFMEDIATYPE);
        response.setHeader("Content-disposition", "inline; filename=" + name);
        response.setContentLength(data.length);
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.getOutputStream().write(data);
        response.getOutputStream().flush();
    }
	
	/**
	 * @author Mukesh Dewangan
	 * @since 30 October 2018
	 * This function will return show article pdf
	 */
	@RequestMapping(value="/showPdfArticle/{articleId}", method=RequestMethod.GET)
	@PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
	public ResponseEntity<byte[]> showPdfArticle(@PathVariable("articleId") String articleId) {
		try
		{
			PFinnContributeArticle article = articleService.get(Integer.parseInt(articleId));
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.parseMediaType(PDFMEDIATYPE));
		    String filename = article.getUpload_id()+"-"+article.getArticleTitle()+".pdf";
		    headers.add("content-disposition", "inline;filename=" + filename);
		    headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		    return new ResponseEntity<>(article.getFile_data(), headers, HttpStatus.OK);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
	}
	/*DiscussionThread discussionThreadObject = discussionForumService.getDiscussionThreadByThreadId(threadId);
	if(discussionThreadObject != null)
	{
		if(discussionThreadObject.getThreadStatus() != null)
		{
			
			Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
			if (!(authentication instanceof AnonymousAuthenticationToken)) 
			{
				PFinnNewUser user = userService.getUserByUserName(authentication.getName());
				discussionForumService.checkIfUserHasViewedThisThread(user, discussionThreadObject);
			}
			
	
	
*/	
}
