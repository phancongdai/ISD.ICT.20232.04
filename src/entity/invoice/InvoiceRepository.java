package entity.invoice;

import java.sql.SQLException;
import java.util.List;

public interface InvoiceRepository {
    void save(Invoice invoice) throws SQLException;
    void updateStatus(int invoiceId, String status) throws SQLException;
    List<Invoice> getAll() throws SQLException;
    Invoice findById(int id) throws SQLException;
}
