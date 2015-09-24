#!/usr/bin/env python3

# -------
# Jung Yoon
# Project 2 - Netflix
# CS 373 - Summer 2015
# Professor Downing
# -------

# -------
# Netflix.py
# -------

from io       import StringIO
from functools import reduce
from math      import sqrt
import json
from urllib.request import urlopen

# ------------
# netflix_predict
# ------------

def netflix_predict (user, movie, total, num) :
    """
    user             the average user rating
    movie           the average movie rating
    total the total average rating for probe
    num      the number of ratings for movie
    returns prediction value in float or int from 1-5
    """

    # edge case: where average ratings not given in cache
    if (type(user) is not int) and (type(user) is not float) and (type(movie) is not int) and (type(movie) is not float) :
        predict = total
    elif (type(user) is not int) and (type(user) is not float) :
        predict = (movie * .95) + (total * .05)
    elif (type(movie) is not int) and (type(movie) is not float) :
        predict = (user * .95) + (total * .05) 
    
    # general predictions
    else :
          if   num < 2 :
              predict = total + ((user - (1.5 * total)) + (movie - (.55 * total)))
          elif num < 5 :
              predict = total + ((user - (.97 * total)) + (movie - (.80 * total))) 
          elif num > 25 :
              predict = total + ((user - (.97 * total)) + (movie - (1 * total)))
          elif num > 50:
              predict = total + ((user - (.90 * total)) + (movie - (1.5 * total)))
          elif num > 300 :
              predict = movie
          else :
              predict = total + ((user - (.97 * total)) + (movie - (.93 * total)))

    # edge case: make sure that ratings are between 1 and 5
    if predict > 5 :
       predict = 5
    elif predict < 1 :
       predict = 1

    # post conditions
    assert (type(predict) is float) or (type(predict) is int)
    assert predict >= 1
    assert predict <= 5

    return predict

# ------------
# netflix_rmse
# ------------

def netflix_rmse (a, p) :
    """
    O(1) in space
    O(n) in time
    """
    assert hasattr(a, "__len__")
    assert hasattr(p, "__len__")
    assert hasattr(a, "__iter__")
    assert hasattr(p, "__iter__")
    z = zip(a, p)
    v = sum((x - y) ** 2 for x, y in z)
    return sqrt(v / len(a))

# -------------
# netflix_print
# -------------

def netflix_print (w, i) :
    """
    w a writer
    i the movie_id
    """
    w.write(str(i) + "\n")

# -------------
# netflix_print_ratings
# -------------

def netflix_print_rating (w, k) :
    """
    w a writer
    k the predicted rating
    """
    w.write("{0:.1f}".format(k) +  "\n")

# -------------
# netflix_print_rmse
# -------------

def netflix_print_rmse (w, j) :
    """
    w a writer
    j the movie_id or the customer_id
    """
    w.write("RMSE: " + "{0:.2f}".format(j) +  "\n")

# -------------
# netflix_solve
# -------------

def netflix_solve (r, w) :
    """
    r a reader
    w a writer
    """

    # open cache files from hard coded address
    cache1 = urlopen("http://www.cs.utexas.edu/~ebanner/netflix-tests/ezo55-Average_Viewer_Rating_Cache.json")
    average_user_rating = json.loads(cache1.read().decode(cache1.info().get_param('charset') or 'utf-8'))

    cache2 = urlopen("http://www.cs.utexas.edu/~ebanner/netflix-tests/BRG564-Average_Movie_Rating_Cache.json")
    average_movie_rating = json.loads(cache2.read().decode(cache2.info().get_param('charset') or 'utf-8'))

    cache3 = urlopen("http://www.cs.utexas.edu/~ebanner/netflix-tests/pam2599-probe_solutions.json")
    probe_rating = json.loads(cache3.read().decode(cache3.info().get_param('charset') or 'utf-8'))
  
    # parses input movie by movie and places into temp_list
    # where first value is movie_id and the rest are customer_id's

    temp_list = []
    actual_predictions = []
    calculated_predictions = []
    count = 0
    total_sum = 0
    probe_average = 0
    total_num = 0

    # calculates total average from probe
    for z in probe_rating.keys() :
        inner = probe_rating[z]
        for e in inner.keys() :
           total_sum += inner[e]
           total_num += 1
    probe_average = total_sum / total_num    

    # goes through input and gets predictions
    for s in r :
        if s[-2] == ':' :
            # makes predictions starting from when second movie is read in
            if count > 0 :
                # handle old list --> make predictions 
                iter_temp = iter(temp_list)
                movie_id = next(iter_temp)
                netflix_print(w, movie_id)
                for user_id in iter_temp :
                    # get average (movie and user) ratings from caches
                    if user_id[-1] == '\n' :
                        u_id = user_id[:-1]
                        m_id = movie_id[:-1]
                    else :
                        u_id = user_id
                        m_id = movie_id[:-1]
                    if u_id in average_user_rating :
                        user_rating = average_user_rating[u_id]
                    else :
                        user_rating = False
                    if m_id in average_movie_rating :
                        movie_rating = average_movie_rating[m_id]
                    else :
                        user_rating = False

                    # use cache values to predict rating
                    prediciton = netflix_predict(user_rating, movie_rating, probe_average, len(temp_list)-1)
                    netflix_print_rating(w, prediciton)

                    # store predicted in list
                    calculated_predictions.append(prediciton)

                    # get actual rating from probe cache and store in list
                    probe_users = probe_rating[str(m_id)]
                    actual_rating = probe_users[str(u_id)]
                    actual_predictions.append(actual_rating)
                temp_list.clear()
                temp_list.append(s[:-1])
            else :
                # setup new list:
                temp_list.clear()
                temp_list.append(s[:-1])
                count += 1
        else :
            temp_list.append(s)
    # handle last list iteration
    iter_temp = iter(temp_list)
    movie_id = next(iter_temp)
    netflix_print(w, movie_id)
    for user_id in iter_temp :
        # get average (movie and user) ratings from caches
        if user_id[-1] == '\n' :
            u_id = user_id[:-1]
            m_id = movie_id[:-1]
        else :
            u_id = user_id
            m_id = movie_id[:-1]
        if u_id in average_user_rating :
            user_rating = average_user_rating[u_id]
        else :
            user_rating = False
        if m_id in average_movie_rating :
            movie_rating = average_movie_rating[m_id]
        else :
            user_rating = False

        # use cache values to predict rating
        prediciton = netflix_predict(user_rating, movie_rating, probe_average, len(temp_list)-1)
        netflix_print_rating(w, prediciton)

        # store predicted in list
        calculated_predictions.append(prediciton)

        # get actual rating from probe cache and store in list
        probe_users = probe_rating[str(m_id)]
        actual_rating = probe_users[str(u_id)]
        actual_predictions.append(actual_rating)

    # print rmse and total ratings
    rmse = netflix_rmse(actual_predictions, calculated_predictions)
    netflix_print_rmse(w, rmse)


