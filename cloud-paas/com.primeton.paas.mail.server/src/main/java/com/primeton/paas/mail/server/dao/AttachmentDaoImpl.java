/**
 * 
 */
package com.primeton.paas.mail.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.mail.server.jdbc.ConnectionFactory;
import com.primeton.paas.mail.server.model.AttachmentInfo;
import com.primeton.paas.mail.server.util.DaoUtil;

/**
 * 
 * @author ZhongWen.Li (mailto:lizw@primeton.com)
 *
 */
public class AttachmentDaoImpl implements AttachmentDao {

	private static ILogger logger = LoggerFactory.getLogger(MailDaoImpl.class);

	private static final String INSERT_SQL = "insert into PAS_MAIL_ATTACHMENT values(?, ?, ?, ?)";
	private static final String DELETE_SQL = "delete from PAS_MAIL_ATTACHMENT";

	private static final String GETALL_SQL = "select ATTACHID,MAILID,PATH,NAME from PAS_MAIL_ATTACHMENT";
	private static final String GETBYID_SQL = GETALL_SQL + " where ATTACHID=?";
	private static final String GETBYMAILID_SQL = GETALL_SQL
			+ " where MAILID=?";

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.mail.server.dao.AttachmentDao#insert(com.primeton.paas.mail.server.model.AttachmentInfo)
	 */
	public boolean insert(AttachmentInfo attachment) {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = ConnectionFactory.getConnection();
			stmt = con.prepareStatement(INSERT_SQL);
			stmt.setString(1, attachment.getAttachId());
			stmt.setString(2, attachment.getMailId());
			stmt.setString(3, attachment.getPath());
			stmt.setString(4, attachment.getName());
			return stmt.execute();
		} catch (Exception e) {
			logger.error("insert attachment Exception" + e.getMessage());
		} finally {
			DaoUtil.close(con, stmt, null);
		}
		return false;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.mail.server.dao.AttachmentDao#deleteAll()
	 */
	public void deleteAll() {
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = ConnectionFactory.getConnection();
			stmt = con.prepareStatement(DELETE_SQL);
			stmt.execute();
		} catch (Exception e) {
			logger.error("delete attachment Exception" + e.getMessage());
		} finally {
			DaoUtil.close(con, stmt, null);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.mail.server.dao.AttachmentDao#get(java.lang.String)
	 */
	public AttachmentInfo get(String attachId) {
		AttachmentInfo att = new AttachmentInfo();
		att.setAttachId(attachId);
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ConnectionFactory.getConnection();
			stmt = con.prepareStatement(GETBYID_SQL);
			stmt.setString(1, attachId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				att.setMailId(rs.getString("MAILID")); //$NON-NLS-1$
				att.setName(rs.getString("NAME")); //$NON-NLS-1$
				att.setPath(rs.getString("PATH")); //$NON-NLS-1$
			}
		} catch (Exception e) {
			logger.error("getbyId attachment Exception" + e.getMessage());
		} finally {
			DaoUtil.close(con, stmt, rs);
		}
		return att;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.mail.server.dao.AttachmentDao#getAttOfMail(java.lang.String)
	 */
	public List<AttachmentInfo> getAttOfMail(String mailId) {
		List<AttachmentInfo> list = new ArrayList<AttachmentInfo>();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ConnectionFactory.getConnection();
			stmt = con.prepareStatement(GETBYMAILID_SQL);
			stmt.setString(1, mailId);
			rs = stmt.executeQuery();
			while (rs.next()) {
				AttachmentInfo att = new AttachmentInfo();
				att.setMailId(mailId);
				att.setAttachId(rs.getString("ATTACHID")); //$NON-NLS-1$
				att.setName(rs.getString("NAME")); //$NON-NLS-1$
				att.setPath(rs.getString("PATH")); //$NON-NLS-1$
				list.add(att);
			}
		} catch (Exception e) {
			logger.error("getofmail attachment Exception" + e.getMessage());
		} finally {
			DaoUtil.close(con, stmt, rs);
		}
		return list;
	}

	/*
	 * (non-Javadoc)
	 * @see com.primeton.paas.mail.server.dao.AttachmentDao#getAll()
	 */
	public List<AttachmentInfo> getAll() {
		List<AttachmentInfo> list = new ArrayList<AttachmentInfo>();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ConnectionFactory.getConnection();
			stmt = con.prepareStatement(GETALL_SQL);
			rs = stmt.executeQuery();
			AttachmentInfo att = new AttachmentInfo();
			while (rs.next()) {
				att.setAttachId(rs.getString("ATTACHID")); //$NON-NLS-1$
				att.setMailId(rs.getString("MAILID")); //$NON-NLS-1$
				att.setName(rs.getString("NAME")); //$NON-NLS-1$
				att.setPath(rs.getString("PATH")); //$NON-NLS-1$
			}
			list.add(att);
		} catch (Exception e) {
			logger.error("getall attachment Exception" + e.getMessage());
		} finally {
			DaoUtil.close(con, stmt, rs);
		}
		return list;
	}

}
