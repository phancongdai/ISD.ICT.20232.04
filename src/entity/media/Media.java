package entity.media;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import entity.db.AIMSDB;

/**
 * The general media class, for another media it can be done by inheriting this class
 * @author nguyendai
 */
public class Media {
    protected Statement stm;
    protected int id;
    protected String title;
    protected String category;
    protected int value; // the real price of product (eg: 450)
    protected int price; // the price which will be displayed on browser (eg: 500)
    protected int quantity;
    protected String type;
    protected String imageURL;

    protected  boolean isSupportedPlaceRushOrder = new Random().nextBoolean();

    public Media() throws SQLException{
        stm = AIMSDB.getConnection().createStatement();
    }

    public Media (int id, String title, String category, int price, int quantity, String type) throws SQLException{
        this.id = id;
        this.title = title;
        this.category = category;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
    }

    public Media(int id, String title, String category, int value, int price, int quantity, String type, String imageURL) {
        this.id = id;
        this.title = title;
        this.category = category;
        this.value = value;
        this.price = price;
        this.quantity = quantity;
        this.type = type;
        this.imageURL = imageURL;
    }

    public int getQuantity() throws SQLException{
        int updated_quantity = getMediaById(id).quantity;
        this.quantity = updated_quantity;
        return updated_quantity;
    }

    public Media getMediaById(int id) throws SQLException{
        String sql = "SELECT * FROM Media";
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery(sql);
		if(res.next()) {
            return new Media()
                .setId(res.getInt("id"))
                .setTitle(res.getString("title"))
                .setQuantity(res.getInt("quantity"))
                .setCategory(res.getString("category"))
                .setMediaURL(res.getString("imageUrl"))
                .setPrice(res.getInt("price"))
                .setType(res.getString("type"));
        }
        return null;
    }

    public List<Media> getAllMedia() throws SQLException{
        Statement stm = AIMSDB.getConnection().createStatement();
        ResultSet res = stm.executeQuery("select * from Media");
        return _extractMediaFromResultSet(res);
    }

    public List<Media> getMediaByType(String type) throws SQLException {
        String sql = "select * from Media where type = ?";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setString(1, type);
        ResultSet res = stm.executeQuery();

        ArrayList<Media> items = _extractMediaFromResultSet(res);
        for (Media media : items) {
            System.out.println(media.quantity);
        }
        return items;
    }

    public void updateMediaFieldById(String tableName, int id, String field, Object value) throws SQLException {
        Statement stm = AIMSDB.getConnection().createStatement();
        if (value instanceof String){
            value = "\"" + value + "\"";
        }

        stm.executeUpdate(" update " + tableName + " set " + field + "=" + value + " where id=" + id);
    }

    public void addNewMedia(String title, String type, String category, String imgUrl, double price, int quantity) throws SQLException {
        String sql = "insert into Media (title, type, category, imageUrl, price, quantity, value) values (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setString(1, title);
        stm.setString(2, type);
        stm.setString(3, category);
        stm.setString(4, imgUrl);
        stm.setDouble(5, price);
        stm.setInt(6, quantity);
        stm.setInt(7, 1);

        stm.executeUpdate();
    }

    public void deleteMediaById(int id) throws SQLException {
        String sql = "delete from Media where id = ?";
        PreparedStatement stm = AIMSDB.getConnection().prepareStatement(sql);
        stm.setInt(1, id);

        stm.executeUpdate();
    }

    private ArrayList<Media> _extractMediaFromResultSet(ResultSet res) throws SQLException {
        ArrayList<Media> items = new ArrayList<>();
        while (res.next()) {
            Media media = new Media()
                    .setId(res.getInt("id"))
                    .setTitle(res.getString("title"))
                    .setQuantity(res.getInt("quantity"))
                    .setCategory(res.getString("category"))
                    .setMediaURL(res.getString("imageUrl"))
                    .setPrice(res.getInt("price"))
                    .setType(res.getString("type"));
            items.add(media);
        }

        return items;
    }

    // getter and setter 
    public int getId() {
        return this.id;
    }

    private Media setId(int id){
        this.id = id;
        return this;
    }

    public String getTitle() {
        return this.title;
    }

    public Media setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getCategory() {
        return this.category;
    }

    public Media setCategory(String category) {
        this.category = category;
        return this;
    }

    public int getPrice() {
        return this.price;
    }

    public Media setPrice(int price) {
        this.price = price;
        return this;
    }

    public String getImageURL(){
        return this.imageURL;
    }

    public Media setMediaURL(String url){
        this.imageURL = url;
        return this;
    }

    public Media setQuantity(int quantity) {
        this.quantity = quantity;
        return this;
    }

    public String getType() {
        return this.type;
    }

    public Media setType(String type) {
        this.type = type;
        return this;
    }

    public  boolean getIsSupportedPlaceRushOrder() {
        return this.isSupportedPlaceRushOrder;
    }

    public  void setIsSupportedPlaceRushOrder(boolean b) { this.isSupportedPlaceRushOrder = b; }

    @Override
    public String toString() {
        return "{" +
            " id='" + id + "'" +
            ", title='" + title + "'" +
            ", category='" + category + "'" +
            ", price='" + price + "'" +
            ", quantity='" + quantity + "'" +
            ", type='" + type + "'" +
            ", imageURL='" + imageURL + "'" +
            "}";
    }    

}