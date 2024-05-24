package Database;
import com.example.phase3.Classes.Albums;
import com.example.phase3.Classes.Photos;
import com.example.phase3.Classes.Users;
import javafx.scene.image.Image;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class DatabaseConnection {
    Connection con;
    public DatabaseConnection(){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers Loaded Successfully");
            String url = "jdbc:mysql://127.0.0.1:3306/cse_412";
            String username = "root";
            String password = "Aryan2191+";
            con = DriverManager.getConnection(url, username , password);

        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean login(String email, String password){
        try{
            int rowCount = 0;
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Users WHERE email='"+email + "' AND password='"+ password + "'";
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                rowCount++;
            }
            if(rowCount == 0) return false;
            return true;
        } catch(Exception e){
            System.out.println(e);
        }
        return true;
    }

    public boolean signin(String email){
        try {
            int rowCount = 0;
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Users WHERE email='"+email + "'";
            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                rowCount++;
            }
            if(rowCount == 0) return true;

            return false;

        } catch(Exception e){
            System.out.println(e);
        }
        return true;
    }

    public String getProfilefName(String email){
        String fName = "";
        try{
            Statement st = con.createStatement();
            String sql = "SELECT first_name FROM Users WHERE email='"+email+"'";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            fName = rs.getString("first_name");

        } catch(Exception e) {
            System.out.println(e);
        }
        return fName;
    }

    public String getProfilelName(String email){
        String lName = "";
        try{
            Statement st = con.createStatement();
            String sql = "SELECT last_name FROM Users WHERE email='"+email+"'";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            lName = rs.getString("last_name");

        } catch(Exception e) {
            System.out.println(e);
        }
        return lName;
    }

    public ArrayList<Albums> getAlbumList(Users currUser){
        ArrayList albumList = new ArrayList<Albums>();
        try{
            Statement st = con.createStatement();
            String sql = "SELECT album_id FROM Albums WHERE user_id = (SELECT user_id FROM Users WHERE email = '" + currUser.getUserEmail() + "')";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                Albums obj = new Albums();
                obj.setAlbumName(rs.getString("album_id"));
                albumList.add(obj);
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return albumList;
    }

    public String getAlbumName(Albums album){
        String name = "";
        try{
            Statement st = con.createStatement();
            String sql = "SELECT name FROM Albums WHERE album_id='" + album.getAlbumName()+ "'";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            return rs.getString("name");
        } catch(Exception e){
            System.out.println(e);
        }
        return name;
    }

    public ArrayList<String> getFriendList(Users currUser){
        ArrayList friends = new ArrayList<String>();
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Users WHERE user_id IN (SELECT friend_id FROM Friends WHERE user_id IN (SELECT user_id FROM Users WHERE email ='" + currUser.getUserEmail()+"'))";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                friends.add(rs.getString("first_name") + " " + rs.getString("last_name"));
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return friends;
    }

    public void addUser(Users currUser){
        try{
            Statement st = con.createStatement();
            st = con.createStatement();
            String sql = "INSERT INTO Users (first_name, last_name, email, dob, gender, hometown, password) VALUES ('"+currUser.getUserfName()+"','"+currUser.getUserlName()+"','"+currUser.getUserEmail()+"','"+currUser.getDob()+"','"+currUser.getGender()+"','"+currUser.getHometown()+"','"+currUser.getPassword()+"')";
            st.executeUpdate(sql);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void addAlbum(Albums currAlbum, Users currUser){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Users WHERE email='"+currUser.getUserEmail()+"'";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            int id = rs.getInt("user_id");
            st = con.createStatement();
            sql = "INSERT INTO Albums (user_id, name, creation_date) VALUES ('" + id + "','" + currAlbum.getAlbumName() + "','"+formatter.format(date)+"')";
            st.executeUpdate(sql);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public boolean albumPresent(String albumName){
        try{
            int size = 0;
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Albums WHERE name ='"+albumName+"'";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                size++;
            }
            if(size == 0){
                return false;
            }
            return true;
        } catch(Exception e){
            System.out.println(e);
        }
        return false;
    }

    public String searchFriendByEmail(String email){
        String name = "";
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Users WHERE email='"+email+"'";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            name = rs.getString("first_name") + " " + rs.getString("last_name");
        } catch(Exception e){
            System.out.println(e);
        }
        return name;
    }

    public boolean searchFriendByName(String fullName, Users currUser){
        String name = "";
        try{
            String[] names = fullName.split(" ");
            Statement st = con.createStatement();
            String sql1 = "SELECT * FROM Users WHERE first_name='"+names[0]+"' AND last_name = '" + names[1]+"'";
            ResultSet rs = st.executeQuery(sql1);
            rs.next();
            int id = rs.getInt("user_id");
            st = con.createStatement();
            String sql2 = "SELECT * FROM Users WHERE email='"+currUser.getUserEmail()+"'";
            rs = st.executeQuery(sql2);
            rs.next();
            int id2 = rs.getInt("user_id");
            st = con.createStatement();
            String sql = "SELECT * FROM Friends WHERE friend_id = '" + id + "' AND user_id = '" + id2 + "'";
            rs = st.executeQuery(sql);
            while(rs.next()){
                return true;
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return false;
    }

    public void addFriend(String fullName, Users currUser){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            Statement st = con.createStatement();
            String sql1 = "SELECT * FROM USERS WHERE email = '" + currUser.getUserEmail() + "'";
            ResultSet rs = st.executeQuery(sql1);
            rs.next();
            int user_id = rs.getInt("user_id");
            st = con.createStatement();
            String[] names = fullName.split(" ");
            String sql2 = "SELECT * FROM Users WHERE first_name = '" + names[0] + "' AND last_name = '" + names[1]+ "'";
            rs = st.executeQuery(sql2);
            rs.next();
            int friend_id = rs.getInt("user_id");
            st = con.createStatement();
            String sql = "INSERT INTO Friends (user_id, friend_id, friendship_date) VALUES ('" + user_id + "','" + friend_id + "','" + formatter.format(date)+"')";
            st.executeUpdate(sql);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public void deleteAlbum(String albumName, Users currUser) {
        try{
            Statement st = con.createStatement();
            String sql3 = "SELECT * FROM Photos WHERE album_id="+getAlbumId(albumName);
            ResultSet rs = st.executeQuery(sql3);
            while(rs.next()){
                int photo_id = rs.getInt("photo_id");
                deletePhotos(photo_id);
            }
            st = con.createStatement();
            int album_id = getAlbumId(albumName);
            String sql2 = "DELETE FROM Photos WHERE album_id = '" + album_id+"'";
            st.executeUpdate(sql2);
            st = con.createStatement();
            String sql1 = "SELECT * FROM Users WHERE email = '" + currUser.getUserEmail() + "'";
            rs = st.executeQuery(sql1);
            rs.next();
            int id = rs.getInt("user_id");
            st = con.createStatement();
            String sql = "DELETE FROM Albums WHERE name = '" + albumName + "' AND user_id = '" + id + "'";
            st.executeUpdate(sql);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public String getUserEmail(String fName, String lName){
        String email = "";
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Users WHERE first_name = '" + fName +"' AND last_name = '" + lName + "'";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            email = rs.getString("email");
        } catch(Exception e){
            System.out.println(e);
        }
        return email;
    }

    public void addPhoto(String caption, String albumName, String url){
        try{
            InputStream in = new FileInputStream(url);
            int albumId = getAlbumId(albumName);
            PreparedStatement ps = con.prepareStatement("INSERT INTO Photos (album_id, caption, photo_data) VALUES (?,?,?)");
            ps.setInt(1, albumId);
            ps.setString(2, caption);
            ps.setBlob(3, in);
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Photos WHERE album_id = '"+albumId+"'";
            ResultSet rs = st.executeQuery(sql);
            ArrayList<Integer> photoId = new ArrayList<Integer>();
            while(rs.next()){
                photoId.add(rs.getInt("photo_id"));
            }
            ps.execute();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public int getAlbumId(String albumName){
        int id = -1;
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Albums WHERE name = '"+albumName+"'";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            id = rs.getInt("album_id");
        } catch(Exception e){
            System.out.println(e);
        }
        return id;
    }

    public List<Photos> viewPhotos(String albumName){
        List<Photos> imgList = new ArrayList<>();
        Image img = null;
        try{
            int album_id = getAlbumId(albumName);
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Photos WHERE album_id = " + album_id + ";";
            ResultSet rs = st.executeQuery(sql);
           while(rs.next()){
               Photos photo = new Photos();
               photo.setPhotoId(rs.getInt("photo_id"));
               photo.setAlbumId(rs.getInt("album_id"));
               photo.setCaption(rs.getString("caption"));
               Blob b = rs.getBlob("photo_data");
               InputStream is = rs.getBinaryStream("photo_data");
               img = new Image(is);
               photo.setPhoto(img);
               imgList.add(photo);
            }
            return imgList;
        } catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    public List<Photos> deletePhotos(int photo_id){
        List<Photos> imgList = new ArrayList<>();
        try{
            Statement st = con.createStatement();
            String sql6 = "DELETE FROM Tags WHERE photo_id="+photo_id;
            st.executeUpdate(sql6);
            st = con.createStatement();
            String sql5 = "DELETE FROM Likes WHERE photo_id="+photo_id;
            st.executeUpdate(sql5);
            st = con.createStatement();
            String sql4 = "DELETE FROM Comments WHERE photo_id="+photo_id;
            st.executeUpdate(sql4);
            st = con.createStatement();
            String sql2 = "SELECT album_id FROM Photos WHERE photo_id = " + photo_id;
            ResultSet rs = st.executeQuery(sql2);
            rs.next();
            int album_id = rs.getInt(1);
            st = con.createStatement();
            String sql = "DELETE FROM Photos WHERE photo_id ="+photo_id;
            st.executeUpdate(sql);
            st = con.createStatement();
            String sql3 = "SELECT * FROM Photos WHERE album_id = "+album_id;
            rs = st.executeQuery(sql3);
            while(rs.next()){
                Photos photo = new Photos();
                photo.setPhotoId(rs.getInt("photo_id"));
                photo.setAlbumId(rs.getInt("album_id"));
                photo.setCaption(rs.getString("caption"));
                InputStream is = rs.getBinaryStream("photo_data");
                photo.setPhoto(new Image(is));
                imgList.add(photo);
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return imgList;
    }

    public void addComment(Photos photo, Users currUser){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            PreparedStatement ps = con.prepareStatement("INSERT INTO Comments (text, user_id, photo_id, comment_date) VALUES (?,?,?,?)");
            ps.setString(1, photo.getComment());
            ps.setInt(2, getUserId(currUser.getUserEmail()));
            ps.setInt(3, photo.getPhotoId());
            ps.setString(4, formatter.format(date));
            ps.execute();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public int getUserId(String userEmail){
        int id = -1;
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Users WHERE email = '"+userEmail+"'";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            id = rs.getInt("user_id");
        } catch(Exception e){
            System.out.println(e);
        }
        return id;
    }

    public String getCaption(int photo_id){
        String caption = null;
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Photos WHERE photo_id="+photo_id;
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            caption = rs.getString("caption");
        } catch(Exception e){
            System.out.println(e);
        }
        return caption;
    }

    public String getComment(int photo_id, Users currUser){
        String comment = null;
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Comments WHERE photo_id="+photo_id+" AND user_id="+getUserId(currUser.getUserEmail());
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            comment = rs.getString("text");
        } catch(Exception e){
            System.out.println(e);
        }
        return comment;
    }

    public boolean userCommentPresent(int photo_id, Users currUser){
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Comments WHERE photo_id="+photo_id+" AND user_id="+getUserId(currUser.getUserEmail());
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                return true;
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return false;
    }

    public void addLike(Photos currPhoto, Users currUser){
        try{
           Statement st = con.createStatement();
           String sql = "INSERT INTO Likes (user_id, photo_id) VALUES ("+getUserId(currUser.getUserEmail())+", "+currPhoto.getPhotoId()+")";
           st.executeUpdate(sql);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public boolean likeFound(int photo_id, Users currUser){
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Likes WHERE photo_id="+photo_id+" AND user_id="+getUserId(currUser.getUserEmail());
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                return true;
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return false;
    }

    public String[] getTopContributorList(){
        HashMap<String, Integer> list = new HashMap<>();
        List<Integer> score = new ArrayList<>();
        String[] sortedNames = new String[10];
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Users";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                list.put(rs.getString("first_name")+" "+rs.getString("last_name"), getContributionScore(getUserId(rs.getString("email"))));
            }
            for(Map.Entry<String, Integer> set : list.entrySet()){
                score.add(set.getValue());
            }
            Collections.sort(score);
            for(int i = 0; i < sortedNames.length; i++){
                sortedNames[i] = "";
            }
            int j = 0;
            for(int i = score.size()-1; i > 0 && j<=10; i--){
                for(Map.Entry<String, Integer> set : list.entrySet()){
                    if(score.get(i) == set.getValue() && nameFound(sortedNames, set.getKey()) == -1){
                        sortedNames[j] = set.getKey();
                        j++;
                    }
                }
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return sortedNames;
    }

    public int nameFound(String[] nameList, String name){
        for(int i = 0; i < nameList.length; i++){
            if(nameList[i].equals(name)){
                return i;
            }
        }
        return -1;
    }
    public int getContributionScore(int user_id){
        int score = 0;
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Comments WHERE user_id="+user_id;
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                score++;
            }
            st = con.createStatement();
            String sql2 = "SELECT * FROM Albums WHERE user_id="+user_id;
            rs = st.executeQuery(sql2);
            while(rs.next()){
                int album_id = rs.getInt("album_id");
                st = con.createStatement();
                String sql3 = "SELECT * FROM Photos WHERE album_id="+album_id;
                ResultSet rs1 = st.executeQuery(sql3);
                while(rs1.next()){
                    score++;
                }
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return score;
    }

    public int likesCount(int photo_id){
        int likes = 0;
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Likes WHERE photo_id="+photo_id;
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                likes++;
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return likes;
    }

    public List<String> friendsMadeComment(String toSearch){
        List<String> list = new ArrayList<>();
        try{
            Statement st = con.createStatement();
            String sql = "SELECT user_id FROM Comments WHERE text='"+toSearch+"'";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                list.add(getFullName(rs.getInt("user_id")));
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return list;
    }

    public String getFullName(int user_id){
        String name = null;
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Users WHERE user_id="+user_id;
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                name = rs.getString("first_name")+" "+rs.getString("last_name");
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return name;
    }

    public void addTag(String tag, int photo_id){
        try{
            Statement st = con.createStatement();
            String sql = "INSERT INTO Tags (photo_id, tag) VALUES ("+photo_id+", '"+tag+"')";
            st.executeUpdate(sql);
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public List<String> getTagList(int photo_id){
        List<String> list = new ArrayList<>();
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Tags WHERE photo_id="+photo_id;
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                list.add(rs.getString("tag"));
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return list;
    }

    public List<Image> getTaggedAllPhotos(String tag, boolean flag, Users currUser){
        String[] diffTags = tag.split(" ");
        List<Image> list = new ArrayList<>();
        List<Integer> photoIds = new ArrayList<>();
        List<Integer> userOnly = new ArrayList<>();
        List<Integer> userPhotoIds = new ArrayList<>();
        List<Image> list2 = new ArrayList<>();
        try{
            Statement st;
            String sql = null;
            ResultSet rs;
            for(int i = 0; i < diffTags.length; i++){
                st = con.createStatement();
                sql = "SELECT * FROM Tags WHERE tag='"+diffTags[i]+"'";
                rs = st.executeQuery(sql);
                while(rs.next()){
                    if(!searchInList(photoIds, rs.getInt("photo_id"))){
                        photoIds.add(rs.getInt("photo_id"));
                    }
                }
            }
            if(!flag){
                List<Integer> albumIds = new ArrayList<>();
                st = con.createStatement();
                sql = "SELECT * FROM Albums WHERE user_id="+getUserId(currUser.getUserEmail());
                rs = st.executeQuery(sql);
                while(rs.next()){
                    albumIds.add(rs.getInt("album_id"));
                }
                for(int i = 0; i < albumIds.size(); i++){
                    st = con.createStatement();
                    sql = "SELECT * FROM Photos WHERE album_id="+albumIds.get(i);
                    rs = st.executeQuery(sql);
                    while(rs.next()){
                        userOnly.add(rs.getInt("photo_id"));
                    }
                }
                for(int i = 0; i < userOnly.size(); i++){
                    if(intFound(photoIds, userOnly.get(i))){
                        userPhotoIds.add(userOnly.get(i));
                    }
                }

                for(int i = 0; i < userPhotoIds.size(); i++){
                    st = con.createStatement();
                    sql = "SELECT * FROM Photos WHERE photo_id="+userPhotoIds.get(i);
                    rs = st.executeQuery(sql);
                    rs.next();
                    Blob blob = rs.getBlob("photo_data");
                    InputStream is = blob.getBinaryStream();
                    Image image = new Image(is);
                    list2.add(image);
                }
                return list2;
            }
            for(int i = 0; i < photoIds.size(); i++){
                st = con.createStatement();
                sql = "SELECT * FROM Photos WHERE photo_id="+photoIds.get(i);
                rs = st.executeQuery(sql);
                rs.next();
                Blob blob = rs.getBlob("photo_data");
                InputStream is = blob.getBinaryStream();
                Image image = new Image(is);
                list.add(image);
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return list;
    }

    public boolean intFound(List<Integer> list, int id){
        for(int i = 0; i < list.size(); i++){
            if(id == list.get(i)){
                return true;
            }
        }
        return false;
    }

    public boolean searchInList(List<Integer> list, int photo_id){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i)==photo_id){
                return true;
            }
        }
        return false;
    }
    public List<String> getPopularTags(){
        List<String> list = new ArrayList<>();
        String[] tags = new String[100];
        int[] count = new int[100];
        for(int i = 0; i < tags.length; i++){
            tags[i] = "";
            count[i] = 0;
        }
        int i = -1;
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Tags";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                if(nameFound(tags, rs.getString("tag")) != -1){
                    count[nameFound(tags, rs.getString("tag"))] = count[nameFound(tags, rs.getString("tag"))]+1;
                } else {
                    i++;
                    tags[i] = rs.getString("tag");
                    count[i]=count[i]+1;
                }
            }
            int[] countCopy = new int[100];
            for(int n = 0; n < countCopy.length; n++){
                countCopy[n] = count[n];
            }
            Arrays.sort(countCopy);
            for(int j = countCopy.length-1; j > countCopy.length-6; j--){
                for(int k = 0; k < count.length; k++){
                    if(countCopy[j] == count[k] && list.size() < 5 && !foundInList(list, tags[k])){
                        list.add(tags[k]);
                    }
                }
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return list;
    }

    public boolean foundInList(List<String> list, String name){
        for(int i = 0; i < list.size(); i++){
            if(list.get(i).equals(name)){
                return true;
            }
        }
        return false;
    }

    public List<Image> viewPopularPhotoByTag(String tag){
        List<Image> list = new ArrayList<>();
        List<Integer> photoId = new ArrayList<>();
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Tags WHERE tag='"+tag+"'";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                photoId.add(rs.getInt("photo_id"));
            }
            sql = null;
            for(int i = 0; i < photoId.size(); i++){
                st = con.createStatement();
                sql = "SELECT * FROM Photos WHERE photo_id="+photoId.get(i);
                rs = st.executeQuery(sql);
                rs.next();
                Blob blob = rs.getBlob("photo_data");
                InputStream is = blob.getBinaryStream();
                Image image = new Image(is);
                list.add(image);
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return list;
    }

    public List<String> getRecommendedFriend(Users currUser){
        List<String> list = new ArrayList<>();
        List<Integer> friends1 = new ArrayList<>();
        List<Integer> friends2 = new ArrayList<>();
        List<Integer> friendsToPrint = new ArrayList<>();
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Friends WHERE user_id="+getUserId(currUser.getUserEmail());
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                friends1.add(rs.getInt("friend_id"));
            }
            for(int i = 0; i < friends1.size(); i++){
                st = con.createStatement();
                sql = "SELECT * FROM Friends WHERE user_id="+friends1.get(i);
                rs = st.executeQuery(sql);
                while(rs.next()){
                    if(!searchInList(friends2, rs.getInt("friend_id"))){
                        friends2.add(rs.getInt("friend_id"));
                    }
                }
            }
            for(int i = 0; i < friends2.size(); i++){
                if(!searchInList(friends1, friends2.get(i)) && friends2.get(i) != getUserId(currUser.getUserEmail())){
                   friendsToPrint.add(friends2.get(i));
                }
            }
            for(int i =0; i < friendsToPrint.size(); i++){
                st = con.createStatement();
                sql = "SELECT * FROM Users WHERE user_id="+friendsToPrint.get(i);
                rs = st.executeQuery(sql);
                while(rs.next()){
                    list.add(rs.getString("first_name")+" "+rs.getString("last_name"));
                }
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return list;
    }

    public void addRecommended(Users currUser, String friendName){
        try{
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            PreparedStatement ps = con.prepareStatement("INSERT INTO Friends (user_id, friend_id, friendship_date) VALUES (?, ?, ?)");
            ps.setInt(1, getUserId(currUser.getUserEmail()));
            ps.setInt(2, getUserIdByName(friendName));
            ps.setString(3, formatter.format(date));
            ps.execute();
        } catch(Exception e){
            System.out.println(e);
        }
    }

    public int getUserIdByName(String name){
        String[] names = name.split(" ");
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Users WHERE first_name='"+names[0]+"' AND last_name='"+names[1]+"'";
            ResultSet rs = st.executeQuery(sql);
            rs.next();
            return rs.getInt("user_id");
        } catch(Exception e){
            System.out.println(e);
        }
        return -1;
    }

    public List<Image> YouMayAlsoLike(Users currUser){
        List<String> allTags = getPopularTags();
        List<String> userTags = new ArrayList<>();
        List<Integer> photo_ids = new ArrayList<>();
        List<Integer> user_photo_ids = new ArrayList<>();
        List<Image> list = new ArrayList<>();
        try{
            Statement st = con.createStatement();
            String sql = "SELECT * FROM Photos WHERE album_id IN (SELECT album_id FROM Albums WHERE user_id="+getUserId(currUser.getUserEmail())+")";
            ResultSet rs = st.executeQuery(sql);
            while(rs.next()){
                user_photo_ids.add(rs.getInt("photo_id"));
            }
            for(int i = 0; i < user_photo_ids.size(); i++){
                st = con.createStatement();
                sql = "SELECT * FROM Tags WHERE photo_id="+user_photo_ids.get(i);
                rs = st.executeQuery(sql);
                while(rs.next()){
                    userTags.add(rs.getString("tag"));
                }
            }
            for(int i = 0; i < allTags.size(); i++){
                if(foundInList(userTags, allTags.get(i))){
                    userTags.add(allTags.get(i));
                }
            }
            for(int i = 0; i < userTags.size(); i++){
                st = con.createStatement();
                sql = "SELECT * FROM Tags WHERE tag='"+userTags.get(i)+"'";
                rs = st.executeQuery(sql);
                while(rs.next()){
                    if(!intFound(photo_ids, rs.getInt("photo_id")) && !intFound(user_photo_ids, rs.getInt("photo_id"))){
                        photo_ids.add(rs.getInt("photo_id"));
                    }
                }
            }
            for(int i = 0; i < photo_ids.size(); i++){
                st = con.createStatement();
                sql = "SELECT * FROM Photos WHERE photo_id="+photo_ids.get(i);
                rs = st.executeQuery(sql);
                rs.next();
                Blob blob = rs.getBlob("photo_data");
                InputStream is = blob.getBinaryStream();
                Image image = new Image(is);
                list.add(image);
            }
        } catch(Exception e){
            System.out.println(e);
        }
        return list;
    }
}