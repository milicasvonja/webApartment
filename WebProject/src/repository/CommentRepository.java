package repository;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import beans.Comment;
import config.PathConfig;

public class CommentRepository {
	
	public static ArrayList<Comment> readComments(String absPath) throws JsonParseException, JsonMappingException, IOException {
		ObjectMapper mapper = new ObjectMapper();
		File commentsFile = new File(absPath  + PathConfig.COMMENTS_FILE);
		boolean created = commentsFile.createNewFile();
		if (created)
			mapper.writeValue(commentsFile, new ArrayList<Comment>());
		return mapper.readValue(commentsFile, new TypeReference<ArrayList<Comment>>() {
		});
	}

	public static void writeComments(String absPath,ArrayList<Comment> comments) throws IOException {
		ObjectMapper mapper = new ObjectMapper();
		File commentsFile = new File(absPath + PathConfig.COMMENTS_FILE);
		commentsFile.createNewFile();
		mapper.writeValue(commentsFile, comments);
	}

	public static Comment getCommentById(String absPath,String id) throws JsonParseException, JsonMappingException, IOException {
		ArrayList<Comment> comments = readComments(absPath);
		for (Comment comment : comments) {
			if (comment.getId().equals(id))
				return comment;
		}
		return null;
	}

}
