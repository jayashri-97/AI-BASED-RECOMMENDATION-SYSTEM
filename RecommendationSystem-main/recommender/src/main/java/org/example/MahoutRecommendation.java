package org.example;

import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.NearestNUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.EuclideanDistanceSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.cf.taste.common.TasteException;

import java.io.File;
import java.util.Iterator;
import java.util.List;

public class MahoutRecommendation {
    public static void main(String[] args) {
        try {
            // Load user-item rating data from CSV file
            DataModel model = new FileDataModel(new File("src\\main\\resources\\data.csv"));

            // Print available users and items for debugging
            printUsersAndItems(model);

            // Compute user similarity (Euclidean Distance - better for small datasets)
            UserSimilarity similarity = new EuclideanDistanceSimilarity(model);

            // Find the nearest 2 neighbors (previously 3, reduced for better recommendations)
            UserNeighborhood neighborhood = new NearestNUserNeighborhood(2, similarity, model);

            // Create a User-Based Recommender
            Recommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);

            // Choose a user for recommendations
            int userID = 4; // Change this if needed

            // Recommend 3 items for the user
            List<RecommendedItem> recommendations = recommender.recommend(userID, 3);

            // Output the recommendations
            System.out.println("\nRecommended items for User " + userID + ":");
            if (recommendations.isEmpty()) {
                System.out.println("No recommendations found for this user.");
            } else {
                for (RecommendedItem recommendation : recommendations) {
                    System.out.println("Item: " + recommendation.getItemID() + " | Score: " + recommendation.getValue());
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Helper function to print users and items in the dataset
    private static void printUsersAndItems(DataModel model) throws TasteException {
        System.out.println("Users in dataset:");
        Iterator<Long> users = model.getUserIDs();
        while (users.hasNext()) {
            System.out.println(users.next());
        }

        System.out.println("\nItems in dataset:");
        Iterator<Long> items = model.getItemIDs();
        while (items.hasNext()) {
            System.out.println(items.next());
        }
        System.out.println();
    }
}

