package entity.payment;

import entity.db.AIMSDB;

import java.sql.*;

public class PaymentTransaction {

	private  int Id;
	private int invoiceId;

	private  int orderId;
	private String createdAt;
	private String errorCode;

	private int amount;
	private String transactionId;
	private String transactionContent;
	private String refundId;
	public PaymentTransaction(String errorCode, String transactionId, String transactionContent,
							  int amount, String createdAt) {
		super();
		this.errorCode = errorCode;


		this.transactionId = transactionId;
		this.transactionContent = transactionContent;
		this.amount = amount;
		this.createdAt = createdAt;
	}

	public PaymentTransaction(int invoiceId, String createdAt, String refundId ){
		this.invoiceId = invoiceId;
		this.createdAt = createdAt;
		this.refundId = refundId;
	};

	public void saveTransaction() throws SQLException {
		Connection connection = AIMSDB.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			String sql = "INSERT INTO `transaction` (invoiceId, createAt , refundId) VALUES (?, ?, ?)";
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setInt(1, this.invoiceId);
			preparedStatement.setString(2, this.createdAt);
			preparedStatement.setString(3,this.refundId);
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			// Đóng PreparedStatement để tránh rò rỉ nguồn và giải phóng tài nguyên
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
	}

	public static String getRefundId(int invoiceId) throws SQLException {
		Connection connection = AIMSDB.getConnection();
		PreparedStatement preparedStatement = null;
		try {
			String sql = "SELECT refundId FROM `Transaction` WHERE invoiceId = ?";
			preparedStatement = connection.prepareStatement(sql);

			preparedStatement.setInt(1,invoiceId);

			ResultSet resultSet = preparedStatement.executeQuery();

			// Kiểm tra xem có dữ liệu trả về hay không
			if (resultSet.next()) {
				String refundId = resultSet.getString("refundId");
				return refundId;
			} else {
				return null;
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			// Đóng PreparedStatement để tránh rò rỉ nguồn và giải phóng tài nguyên
			if (preparedStatement != null) {
				preparedStatement.close();
			}
		}
	}


}
