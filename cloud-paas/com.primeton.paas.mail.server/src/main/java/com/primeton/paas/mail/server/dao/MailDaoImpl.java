package com.primeton.paas.mail.server.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import org.gocom.cloud.common.logger.api.ILogger;
import org.gocom.cloud.common.logger.api.LoggerFactory;

import com.primeton.paas.mail.server.config.Constants;
import com.primeton.paas.mail.server.jdbc.ConnectionFactory;
import com.primeton.paas.mail.server.model.MailInfo;
import com.primeton.paas.mail.server.util.DaoUtil;

public class MailDaoImpl implements MailDao {
	//
	private static ILogger logger = LoggerFactory.getLogger(MailDaoImpl.class);
	
	/**��ѯȫ��*/
	private static final String GETALL_SQL = "select  MAILID,APPNAME,MAILSERVERHOST,MAILSERVERPORT,VALIDATE,IFBACK,USERNAME,PASSWORD,SEND_FROM,SEND_TO,SEND_CC,SUBJECT,CONTENTTYPE,CONTENT,ATTACHMENTSID,CREATEDATE,STATUS,EXCEPTIONMESSAGE,EXCEPTIONCODE from PAS_MAIL";
	/**��ѯδ�����Ҳ��践�ؽ����ʼ�*/
	private static final String GETFORMAILWORKER_SQL = "select * from pas_mail where STATUS=? and IFBACK=?";
	/**����һ���ʼ���Ϣ*/
	private static final String INSERT_SQL = "insert into PAS_MAIL (MAILID,APPNAME,MAILSERVERHOST,MAILSERVERPORT,VALIDATE,IFBACK,USERNAME,PASSWORD,SEND_FROM,SEND_TO,SEND_CC,SUBJECT,CONTENTTYPE,CONTENT,ATTACHMENTSID,CREATEDATE,STATUS) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	/**�����ʼ�״̬*/
	private static final String UPDATE_SQL = "update PAS_MAIL set STATUS=? ,EXCEPTIONCODE =?,EXCEPTIONMESSAGE=? where MAILID=?";
	/**ɾ�����*/
	private static final String DELETE_SQL = "delete from PAS_MAIL";

//	@Override
	public boolean insert(MailInfo mail) {
		// ����һ���ʼ���Ϣ
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = ConnectionFactory.getConnection();
			stmt = con.prepareStatement(INSERT_SQL);
			stmt.setString(1, mail.getMailId());
			stmt.setString(2, mail.getAppName());
			stmt.setString(3, mail.getMailServerHost());
			stmt.setString(4, mail.getMailServerPort());
			stmt.setBoolean(5, mail.isValidate());
			stmt.setBoolean(6, mail.isIfBack());
			stmt.setString(7, mail.getUsername());
			stmt.setString(8, mail.getPassword());
			stmt.setString(9, mail.getSendFrom());
			String toStr= DaoUtil.listToString(mail.getSendTo());// list
			String ccStr = DaoUtil.listToString(mail.getSendCc());// list
			stmt.setString(10, toStr);
			stmt.setString(11, ccStr);
			stmt.setString(12, mail.getSubject());
			stmt.setString(13, mail.getContentType());
			stmt.setString(14, mail.getContent());
			String attachIds = DaoUtil.listToString(mail.getAttachmentsId());// list
			stmt.setString(15, attachIds);
			stmt.setTimestamp(16, new java.sql.Timestamp(mail.getCreateDate().getTime()));
			stmt.setString(17, Constants.SENDR_ESULT_NOTSEND);
			stmt.executeUpdate();
			return true;
		} catch (Exception e) {
			logger.info("insert mail Exception��" + e.getMessage());
		} finally {
			DaoUtil.close(con, stmt, null);
		}
		return false;
	}

//	@Override
	public void update(MailInfo mail) {
		// ����ʼ�״̬
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = ConnectionFactory.getConnection();
			stmt = con.prepareStatement(UPDATE_SQL);
			stmt.setString(1, mail.getStatus());
			stmt.setString(2, mail.getExceptionCode());
			stmt.setString(3, mail.getExceptionMessage());
			stmt.setString(4, mail.getMailId());
			stmt.executeUpdate();
		} catch (Exception e) {
			logger.info("update mail Exception��" + e.getMessage());
		}finally {
			DaoUtil.close(con, stmt, null);
		}
	}

//	@Override
	public void deleteAll() {
		// ���
		Connection con = null;
		PreparedStatement stmt = null;
		try {
			con = ConnectionFactory.getConnection();
			stmt = con.prepareStatement(DELETE_SQL);
			stmt.execute();
		} catch (Exception e) {
			logger.info("delete mail Exception��" + e.getMessage());
		}finally {
			DaoUtil.close(con, stmt, null);
		}
	}

	// @Override
	public List<MailInfo> getAll() {
		// ��������
		List<MailInfo> list = new ArrayList<MailInfo>();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ConnectionFactory.getConnection();
			stmt = con.prepareStatement(GETALL_SQL);
			rs = stmt.executeQuery();
			MailInfo mi = new MailInfo();
			while (rs.next()) {
				mi = DaoUtil.convertToMailInfo(rs);
				list.add(mi);
			}
		} catch (Exception e) {
			logger.info("getall mail Exception��" + e.getMessage());
		}finally {
			DaoUtil.close(con, stmt, rs);
		}
		return list;
	}

//	@Override
	public MailInfo getForMailWorker() {
		// ��ѯ��ݿ� ��ȡ��һ���ʼ�����
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		try {
			con = ConnectionFactory.getConnection();
			stmt = con.prepareStatement(GETFORMAILWORKER_SQL);
			stmt.setString(1, Constants.SENDR_ESULT_NOTSEND);
			stmt.setBoolean(2, false);
			rs = stmt.executeQuery();
			// ��ȡһ��
			while (rs.next()) {
				if(rs!=null){
					return DaoUtil.convertToMailInfo(rs);
				}
			}
		} catch (Exception e) {
			logger.info("getformailworker mail Exception:" + e.getMessage());
			logger.info("getformailworker��"+e.getCause().toString());
		}finally {
			DaoUtil.close(con, stmt, rs);
		}
		return null;
	}

}
