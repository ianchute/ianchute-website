(ns website.core
  (:require [reagent.core :as r]
            [reagent.session :as session]
            [secretary.core :as secretary :include-macros true]
            [goog.events :as events]
            [goog.history.EventType :as HistoryEventType]
            [markdown.core :refer [md->html]]
            [website.ajax :refer [load-interceptors!]]
            [ajax.core :refer [GET POST]])
  (:import goog.History))

(defn nav-link [uri title page]
  [:li.nav-item
   {:class (when (= page (session/get :page)) "active")}
   [:a.nav-link
    {:href uri} title]])

(defn navbar []
  (fn []
    [:nav.navbar.navbar-default
      [:div.collapse.navbar-toggleable-xs.in
      [:a.navbar-brand {:href "#/"} 
        [:img.logo {:src (str js/context "/img/icon.png")}] 
        "Ian Chu Te"]
      [:ul.nav.navbar-nav
        [nav-link "#/" "Home" :home]
        [nav-link "#/experiments" "Experiments" :experiments]
        ; [nav-link "#/games" "Games" :games]
        [nav-link "#/about" "About" :about]]]]))

(defn home-page []
  [:div.container
    [:div.row.card-main
      [:div.col-lg-4.col-md-5.col-sm-6.card-section
        [:p.speech.welcome "Welcome to my website!"]
        [:img.profile {:src (str js/context "/img/me.jpg")}]
        [:img.profile-sticker {:src (str js/context "/img/sticker.png")}] 
        [:h2.my-name "Ian Herve U. Chu Te"]
        [:h4 {:style {:font-style "italic"}} "Data Scientist, Web Developer and Amateur Musician"]
        [:h4 {:style {:text-align "justify"}} "Hi there! This simple website showcases my personal portfolio. You can find my work in data science, machine learning, artificial intelligence and music here."]
        [:h4 {:style {:text-align "justify"}} "You can also find fun web-based games here. The opponent you are playing with is trained using various methods in reinforcement learning and classical machine learning."]
        [:h4 {:style {:text-align "justify"}} "This very website also showcases my web development skills. I built and designed it using the Luminus web framework."]
        [:h4 "Enjoy!"]
      ]
      [:div.col-lg-4.col-md-4.col-sm-3.card-section.my-menu
        [:h3 "My Work"]
        [:a {:href "#/experiments"}
          [:img {:src (str js/context "/img/experiments.png")}] "ML Experiments"]
        [:a.disabled {:href "#/games"}
          [:img {:src (str js/context "/img/games.png")}] "Games and AI (coming soon)"]
        [:a {:href "https://musescore.com/user/25292271"}
          [:img {:src (str js/context "/img/music.png")}] "Amateur Compositions"]
        [:a {:href "https://www.kaggle.com/ianchute/datasets"}
          [:img {:src (str js/context "/img/data.png")}] "Datasets"]
      ]
      [:div.col-lg-4.col-md-3.col-sm-3.card-section.my-menu
        [:h3 "Links"]
        [:a {:href "https://www.linkedin.com/in/ianchute/"} 
          [:img {:src (str js/context "/img/social/linkedin.png")}] "LinkedIn"]
        [:a {:href "https://github.com/ianchute"} 
          [:img {:src (str js/context "/img/social/github.png")}] "GitHub"]
        [:a {:href "https://www.quora.com/profile/Ian-Chu-Te"} 
          [:img {:src (str js/context "/img/social/quora.png")}] "Quora"]
        [:a {:href "https://www.youtube.com/channel/UCg_8JmfWVOHDC2xJdyZSvrQ"} 
          [:img {:src (str js/context "/img/social/youtube.png")}] "YouTube"]
        [:a {:href "https://twitter.com/yanchutz"} 
          [:img {:src (str js/context "/img/social/twitter.png")}] "Twitter"]
        [:a {:href "https://www.instagram.com/chutzzz"} 
          [:img {:src (str js/context "/img/social/instagram.png")}] "Instagram"]
        [:a {:href "https://web.facebook.com/chutzzz"} 
          [:img {:src (str js/context "/img/social/facebook.png")}] "Facebook"]
      ]
  ]])

(defn about-page []
  [:div.timeline
    [:div.container.left
      [:div.content
        [:h2 "2017"]
        [:p "-"]]]
    [:div.container.right
      [:div.content
        [:h2 "2016"]
        [:p "-"]]]
    [:div.container.left
      [:div.content
        [:h2 "2015"]
        [:p "-"]]]
    [:div.container.right
        [:div.content
          [:h2 "2014"]
          [:p "-"]]]
])

(defn experiments-page []
  [:div.container
    [:div.row
    [:div.col-md-12.card-section.my-menu
      [:h3 "Machine Learning Experiments"]
      [:a {:href 
        "#/notebook/aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL2lhbmNodXRlL2RvdGEyLWthZ2dsZS9tYXN0ZXIvZG90YTIta2FnZ2xlLmlweW5i"} 
        [:img {:src (str js/context "/img/experiments/dota-2.png")}] "Predicting Dota 2 Wins"]
      [:a {:href 
        "#/notebook/aHR0cHM6Ly9yYXcuZ2l0aHVidXNlcmNvbnRlbnQuY29tL2lhbmNodXRlL3RydW1wLXR3ZWV0ZXIvbWFzdGVyL3RydW1wLmlweW5i"} 
        [:img {:src (str js/context "/img/experiments/trump.png")}] "Tweeting like Trump"]
    ]]])

(defn games-page []
  [:div.container
    [:div.row
    [:div.col-md-12]]])

(defn notebook-page []
  [:div.container
    [:div.row
    [:div#notebook.col-md-12
  
  ]]])

(def pages
  {:home #'home-page
  :experiments #'experiments-page
  :games #'games-page
  :about #'about-page
  :notebook #'notebook-page})

(defn page []
  [(pages (session/get :page))])

;; -------------------------
;; Routes
(secretary/set-config! :prefix "#")

(secretary/defroute "/" []
  (session/put! :page :home))

(secretary/defroute "/about" []
  (session/put! :page :about))

(secretary/defroute "/experiments" []
  (session/put! :page :experiments))

(secretary/defroute "/notebook/:id" [id]
  (session/put! :page :notebook)
  (.getJSON js/jQuery (str (js/atob id)) (fn [data] 
    (.appendChild 
      (.getElementById js/document "notebook")
        (.render (.parse js/nb data))
    )
  ))
)

(secretary/defroute "/games" []
  (session/put! :page :games))
  

;; -------------------------
;; History
;; must be called after routes have been defined
(defn hook-browser-navigation! []
  (doto (History.)
        (events/listen
          HistoryEventType/NAVIGATE
          (fn [event]
              (secretary/dispatch! (.-token event))))
        (.setEnabled true)))

;; -------------------------
;; Initialize app
(defn mount-components []
  (r/render [#'navbar] (.getElementById js/document "navbar"))
  (r/render [#'page] (.getElementById js/document "app")))

(defn init! []
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
