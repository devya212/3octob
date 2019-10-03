package com.nucleus.dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.nucleus.pojo.DiscussionThread;
import com.nucleus.pojo.PFinnContributeArticle;
import com.nucleus.pojo.PFinnNewUser;

/**
 * @author Vasu Sharma
 * @since 20 September 2018
 */
@Repository
public class ArticleDaoImpl implements ArticleDao {

	@PersistenceContext
	private EntityManager entityManager;

	/**
	 * @author Vasu Sharma
	 * @since 20 September 2018
	 */

	// saving a document in database
	@Override
	public void save(PFinnContributeArticle uploadFile) {
		entityManager.persist(uploadFile);
	}

	/**
	 * @author Vasu Sharma
	 * @since 20 September 2018
	 */

	// getting a document from database
	@Override
	public PFinnContributeArticle get(int uploadId) {
		return entityManager.find(PFinnContributeArticle.class, uploadId);
	}

	/**
	 * @author Vasu Sharma, Ajita Mittal
	 * @return : It will return the list of articles which are pending for approval/rejection by admin
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PFinnContributeArticle> getAllPendingArticles() {
		return entityManager.createQuery("from PFinnContributeArticle where status is null").getResultList();
	}

	/**
	 * @author Vasu Sharma, Ajita Mittal
	 * @return : It will return the list of articles which are approved by admin
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<PFinnContributeArticle> getAllApprovedArticles() {
		return entityManager.createQuery("from PFinnContributeArticle where status= 'APPROVED' ").getResultList();
	}
	
	/**
	 * 	@author Vasu Sharma, Ajita Mittal
	 *  @param 	1> Model attribute - pfinnContributeArticlesList (List<PFinnContributeArticle>)
	 * 	@return : It will update the list of articles which are pending for approval/rejection by admin by removing 
	 * 			  those articles which are either approved or rejected 
	 */
	@Override
	public void updateUserContributionArticleList(List<PFinnContributeArticle> pfinnContributeArticlesList) {
		for (PFinnContributeArticle pFinnContributeArticle : pfinnContributeArticlesList) {
			PFinnContributeArticle pFinnContributeArticleObject = entityManager.find(PFinnContributeArticle.class,
					pFinnContributeArticle.getUpload_id());
			
			pFinnContributeArticleObject.setStatus(pFinnContributeArticle.getStatus());
			pFinnContributeArticleObject.setRemark(pFinnContributeArticle.getRemark());
			pFinnContributeArticleObject.setStatusDate(pFinnContributeArticle.getStatusDate());
			pFinnContributeArticleObject.setArticleTitle(pFinnContributeArticle.getArticleTitle());
			pFinnContributeArticleObject.setArticleDescription(pFinnContributeArticle.getArticleDescription());
		}
	}

	@Override
	public void addUserToViewListOfArticleThread(PFinnNewUser user, PFinnContributeArticle pfinnContributeArticle) {
		PFinnContributeArticle pfinnContributeArticle1  = entityManager.find(PFinnContributeArticle.class, pfinnContributeArticle.getUpload_id());
		List<PFinnNewUser> users = pfinnContributeArticle1.getUsersWhoHaveViewedThisThread();
		users.add(user);
		pfinnContributeArticle1.setUsersWhoHaveViewedThisThread(users);
	}

	@Override
	public void addUserToLikeListOfArticleThread(PFinnNewUser user, PFinnContributeArticle pfinnContributeArticle) {
		PFinnContributeArticle pfinnContributeArticle1  = entityManager.find(PFinnContributeArticle.class, pfinnContributeArticle.getUpload_id());
		List<PFinnNewUser> users = pfinnContributeArticle1.getUsersWhoHaveLikedThisThread();
		users.add(user);
		pfinnContributeArticle1.setUsersWhoHaveLikedThisThread(users);
		
	}

	/*@Override
	public void addUserToViewListOfDiscussionThread(PFinnNewUser user, DiscussionThread discussionThread) {
		DiscussionThread mainDiscussionThread = entityManager.find(DiscussionThread.class, discussionThread.getDiscussionThreadId());
		List<PFinnNewUser> users = mainDiscussionThread.getUsersWhoHaveViewedThisThread();
		users.add(user);
		mainDiscussionThread.setUsersWhoHaveViewedThisThread(users);
	}*/

	
	@Transactional
	@Override
	public List<PFinnContributeArticle> searchArticles(String searchString) {
		
		/*return entityManager.createQuery("select distinct t from DiscussionPost p right join p.discussionThread t"
				+ " where (t.threadStatus=:openStatus or t.threadStatus=:closeStatus) and"
				+ " (LOWER(t.title) like concat('%', LOWER(:searchString), '%') OR"
				+ " LOWER(t.description) like concat('%', LOWER(:searchString), '%') OR "
				+ "LOWER(p.postContent) like concat('%', LOWER(:searchString), '%') ) order by t.postedDate desc", DiscussionThread.class)
				.setParameter("openStatus", "OPEN")
				.setParameter("closeStatus", "CLOSED")
				.setParameter("searchString", searchString)
				.getResultList();
		*/
		
return entityManager.createQuery("select * from PFinnContributeArticle where (LOWER(articleTitle) like concat('%',LOWER(:searchString),'%') OR LOWER(articleDescription) like concat('%',LOWER(:searchString),'%')) AND status= 'APPROVED' order by upload_date desc").setParameter("searchString", searchString).getResultList();
		
	}
	
	
	
}
