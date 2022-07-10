package sg.edu.np.madgroupyassignment;

public class Comments {
    private String parentPost;
    private String commentUID;
    private String commentOwner;
    private String commentPFP;
    private String commentUsername;
    private String commentText;
    private Long timestamp;

    public Comments() {
    }

    public Comments(String parentPost, String commentUID, String commentOwner, String commentPFP, String commentUsername, String commentText, Long timestamp) {
        this.parentPost = parentPost;
        this.commentUID = commentUID;
        this.commentOwner = commentOwner;
        this.commentPFP = commentPFP;
        this.commentUsername = commentUsername;
        this.commentText = commentText;
        this.timestamp = timestamp;
    }

    public String getParentPost() {
        return parentPost;
    }

    public void setParentPost(String parentPost) {
        this.parentPost = parentPost;
    }

    public String getCommentUID() {
        return commentUID;
    }

    public void setCommentUID(String commentUID) {
        this.commentUID = commentUID;
    }

    public String getCommentOwner() {
        return commentOwner;
    }

    public void setCommentOwner(String commentOwner) {
        this.commentOwner = commentOwner;
    }

    public String getCommentPFP() {
        return commentPFP;
    }

    public void setCommentPFP(String commentPFP) {
        commentPFP = commentPFP;
    }

    public String getCommentUsername() {
        return commentUsername;
    }

    public void setCommentUsername(String commentUsername) {
        commentUsername = commentUsername;
    }

    public String getCommentText() {
        return commentText;
    }

    public void setCommentText(String commentText) {
        commentText = commentText;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
}
