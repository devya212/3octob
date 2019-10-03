package com.nucleus.dao;

import java.util.List;

import com.nucleus.pojo.DiscussionThread;
import com.nucleus.pojo.PFinnContributeArticle;
import com.nucleus.pojo.PFinnNewUser;

/**
 * @author Vasu Sharma
 * @since 20 September 2018
 */
public interface ArticleDao {
	public void save(PFinnContributeArticle uploadFile);

	public PFinnContributeArticle get(int uploadId);

	public List<PFinnContributeArticle> getAllPendingArticles();

	public List<PFinnContributeArticle> getAllApprovedArticles();

	void updateUserContributionArticleList(List<PFinnContributeArticle> pfinnContributeArticlesList);

	public void addUserToViewListOfArticleThread(PFinnNewUser user, PFinnContributeArticle pfinnContributeArticle);

	public void addUserToLikeListOfArticleThread(PFinnNewUser user, PFinnContributeArticle pfinnContributeArticle);
	
	public List<PFinnContributeArticle> searchArticles(String searchString);
}
