package com.nucleus.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nucleus.dao.ArticleDao;
import com.nucleus.pojo.DiscussionPost;
import com.nucleus.pojo.DiscussionThread;
import com.nucleus.pojo.PFinnContributeArticle;
import com.nucleus.pojo.PFinnNewUser;
import com.nucleus.utility.DiscussionForumThreadListView;

/**
 * @author Vasu Sharma
 * @since 20 September 2018
 */
@Service
public class ArticleServiceImpl implements ArticleService {

	@Autowired
	private ArticleDao articleDao;

	/**
	 * @author Vasu Sharma
	 * @since 20 September 2018
	 */
	@Override
	@Transactional
	public void save(PFinnContributeArticle uploadFile) {
		articleDao.save(uploadFile);
	}

	/**
	 * @author Vasu Sharma
	 * @since 20 September 2018
	 */
	@Transactional(readOnly = true)
	@Override
	public PFinnContributeArticle get(int uploadId) {
		return articleDao.get(uploadId);
	}

	/**
	 * @author Vasu Sharma, Ajita Mittal
	 * @return : It will return the list of articles which are pending for approval/rejection by admin
	 */
	@Transactional(readOnly = true)
	@Override
	public List<PFinnContributeArticle> getAllPendingArticles() {
		return articleDao.getAllPendingArticles();
	}

	/**
	 * @author Vasu Sharma, Ajita Mittal
	 * @return : It will return the list of articles which are approved by admin
	 */
	@Transactional(readOnly = true)
	@Override
	public List<PFinnContributeArticle> getAllApprovedArticles() {
		return articleDao.getAllApprovedArticles();
	}
	
	/**
	 * 	@author Vasu Sharma, Ajita Mittal
	 *  @param 	1> Model attribute - pfinnContributeArticlesList (List<PFinnContributeArticle>)
	 * 	@return : It will update the list of articles which are pending for approval/rejection by admin by removing 
	 * 			  those articles which are either approved or rejected 
	 */
	@Transactional
	@Override
	public void updateUserContributionArticleList(List<PFinnContributeArticle> pfinnContributeArticlesList) {
		for (PFinnContributeArticle pFinnContributeArticle : pfinnContributeArticlesList) {
			if (pFinnContributeArticle.getStatus() != null && !pFinnContributeArticle.getStatus().equalsIgnoreCase(""))
				pFinnContributeArticle.setStatusDate(new Date(System.currentTimeMillis()));
		}
		articleDao.updateUserContributionArticleList(pfinnContributeArticlesList);
		
	}	
	@Override
	@Transactional(readOnly=false)
	public void checkIfUserHasViewedThisArticle(PFinnNewUser user, PFinnContributeArticle pfinnContributeArticle) {
		boolean alreadyViewed = false;
		for(PFinnNewUser user_i : pfinnContributeArticle.getUsersWhoHaveViewedThisThread()){
			if(user_i.getUsername().equals(user.getUsername())){
				alreadyViewed = true;
				break;
			}
		}
		if (!alreadyViewed)
			articleDao.addUserToViewListOfArticleThread(user, pfinnContributeArticle);
	}

	@Override
	@Transactional(readOnly=false)
	public void checkIfUserHasLikedThisArticle(PFinnNewUser user, PFinnContributeArticle pfinnContributeArticle) {
		boolean alreadyLiked = false;
		for(PFinnNewUser user_i : pfinnContributeArticle.getUsersWhoHaveLikedThisThread()){
			if(user_i.getUsername().equals(user.getUsername())){
				alreadyLiked = true;
				break;
			}
		}
		if (!alreadyLiked)
			articleDao.addUserToLikeListOfArticleThread(user, pfinnContributeArticle);
	}

	@Override
	@Transactional(readOnly=false)
	public List<PFinnContributeArticle> searchArticles(String searchString) {
		/*List<PFinnContributeArticle> discussionForumThreadListView = new ArrayList<>();
		for(PFinnContributeArticle pfinnContributeArticle: articleDao.searchArticles(searchString))
		{
			List<DiscussionPost> totalApprovedPost = this.getApprovedDeletedPostsOfThread(discussionThread, 0, 0);
			DiscussionPost lastPost = null;
			int totalNoOfApprovedPost = totalApprovedPost.size();
			if(totalApprovedPost != null && !totalApprovedPost.isEmpty())
				lastPost = totalApprovedPost.get(totalApprovedPost.size()-1);
			discussionForumThreadListView.add(new DiscussionForumThreadListView(discussionThread, lastPost, totalNoOfApprovedPost));
		}*/
		return  articleDao.searchArticles(searchString);
		
	}

}
