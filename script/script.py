import pickle
import pandas as pd
import sys
import os
import json
from fuzzywuzzy import process
 
script_dir = os.path.dirname(os.path.abspath(__file__))
movies_pkl_path = os.path.join(script_dir, 'movies.pkl')
similarity_pkl_path = os.path.join(script_dir, 'similarity.pkl')

movies_list = pickle.load(open(movies_pkl_path, 'rb'))
similarity = pickle.load(open(similarity_pkl_path, 'rb'))

movies_df = pd.DataFrame(movies_list)

def recommend(movie):
    movie_options = movies_df['title'].tolist()
    closest_match, score = process.extractOne(movie, movie_options)
    
    if score < 90:
        return [{'title': 'Movie not found', 'id': None}]
    
    movie_idx = movies_df[movies_df['title'] == closest_match].index
    distances = similarity[movie_idx[0]]
    recommendations = []
    input_movie_id = int(movies_df.iloc[movie_idx[0]]['movie_id'])
    input_movie_title = movies_df.iloc[movie_idx[0]]['title']
    recommendations.append({'title': input_movie_title, 'id': input_movie_id})
    for i in sorted(list(enumerate(distances)), reverse=True, key=lambda x: x[1])[1:6]:
        movie_id = int(movies_df.iloc[i[0]]['movie_id'])
        movie_title = movies_df.iloc[i[0]]['title']
        recommendation = {'title': movie_title, 'id': movie_id}
        recommendations.append(recommendation)
    return recommendations



if __name__ == "__main__":
    movie_name = sys.argv[1]
    recommendations = recommend(movie_name)
    recommendations_json = json.dumps(recommendations)
    print(recommendations_json, end='')
