package com.nucleus.service;

import java.util.List;

import com.nucleus.pojo.DiscussionThread;
import com.nucleus.pojo.PFinnContributeArticle;
import com.nucleus.pojo.PFinnNewUser;

/**
 * @author Vasu Sharma
 * @since 20 September 2018
 */
public interface ArticleService {
	public void save(PFinnContributeArticle uploadFile);

	public PFinnContributeArticle get(int uploadId);

	public List<PFinnContributeArticle> getAllPendingArticles();

	public List<PFinnContributeArticle> getAllApprovedArticles();

	public void updateUserContributionArticleList(List<PFinnContributeArticle> pfinnContributeArticlesList);

	void checkIfUserHasViewedThisArticle(PFinnNewUser user, PFinnContributeArticle pfinnContributeArticle);

	public void checkIfUserHasLikedThisArticle(PFinnNewUser user, PFinnContributeArticle pfinnContributeArticle);
	
	public List<PFinnContributeArticle> searchArticles(String searchString);
	
}
