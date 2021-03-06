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

(defn ready [f] (.ready (js/$ js/document) f))

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
        [nav-link "#/compositions" "Compositions" :compositions]
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
        [:a {:href "#/compositions"}
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
    [:div.container.right
      [:div.content
        [:h2 "2020"]
        [:p "-"]]]
    [:div.container.left
      [:div.content
        [:h2 "2019"]
        [:p "-"]]]
    [:div.container.right
      [:div.content
        [:h2 "2018"]
        [:p "-"]]]
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
        (str "#/notebook/" (js/btoa "https://raw.githubusercontent.com/ianchute/dota2-kaggle/master/dota2-kaggle.ipynb"))} 
        [:img {:src (str js/context "/img/experiments/dota-2.png")}] "Predicting Dota 2 Wins"]
      [:a {:href 
        (str "#/notebook/" (js/btoa "https://raw.githubusercontent.com/ianchute/trump-tweeter/master/trump.ipynb"))} 
        [:img {:src (str js/context "/img/experiments/trump.png")}] "Tweeting like Trump"]
    ]]

    [:div.row
    [:div.col-md-12.card-section.my-menu
      [:h3 "GitHub Projects"]
      [:a {:href "https://github.com/ianchute/LambdaML"} 
        [:img {:src (str js/context "/img/social/github.png")}] "LambdaML: A lambda-based machine learning library"]
      [:a {:href "https://github.com/ianchute/shapley-attribution-model"} 
        [:img {:src (str js/context "/img/social/github.png")}] "Shapley Value Methods for Attribution Modeling"]
      [:a {:href "https://github.com/ianchute/goalpost-detector"} 
        [:img {:src (str js/context "/img/social/github.png")}] "Goalpost Detector: Using traditional CV techniques"]
      [:a {:href "https://github.com/ianchute/extract_image_data"} 
        [:img {:src (str js/context "/img/social/github.png")}] "Images2CSV: Fast multithreaded script for converting a large number of images to CSV data"]
      [:a {:href "https://github.com/ianchute/toady"} 
        [:img {:src (str js/context "/img/social/github.png")}] "toady: Easily visualize high-dimensional data in 2d space"]
      [:a {:href "https://github.com/ianchute/StockCache/blob/master/chutychart.js"} 
        [:img {:src (str js/context "/img/social/github.png")}] "chutychart.js: A stock data graphing and analysis library"]
    ]]
  ])

(defn games-page []
  [:div.container
    [:div.row
    [:div#game.col-md-12]]])

(defonce notebook-data (r/atom nil))

(defn notebook-page []
  [:div.container
    [:div.row
    [:div#notebook.col-md-12 {:dangerouslySetInnerHTML #js{:__html @notebook-data}}]]])

(defn compositions-page []
  [:div.container
    [:div.row
    [:div.col-md-12.card-section.my-menu
      [:h3 "Compositions"]
      [:a {:href "https://www.youtube.com/watch?v=0bhL6w0ueuw"} 
        [:img {:src (str js/context "/img/music.png")}] "Wilted Roses"]
      [:a {:href "https://www.youtube.com/watch?v=IQZD-bu7dhE"} 
        [:img {:src (str js/context "/img/music.png")}] "Frozen Lake"]
      [:a {:href "https://www.youtube.com/watch?v=m0nvlX4sl4c"} 
        [:img {:src (str js/context "/img/music.png")}] "An Empty Epiphany"]
      [:a {:href 
        (str "#/music/" (js/btoa "https://musescore.com/user/25292271/scores/4583261/embed"))} 
        [:img {:src (str js/context "/img/music.png")}] "Elegy in D Minor"]
      [:a {:href 
        (str "#/music/" (js/btoa "https://musescore.com/user/25292271/scores/4724766/embed"))} 
        [:img {:src (str js/context "/img/music.png")}] "Elegy in F# Minor \"Rain Dance\""]
      [:a {:href 
        (str "#/music/" (js/btoa "https://musescore.com/user/25292271/scores/4691681/embed"))} 
        [:img {:src (str js/context "/img/music.png")}] "Rhapsody for Viola and Piano in A Minor"]
      [:a {:href 
        (str "#/music/" (js/btoa "https://musescore.com/user/25292271/scores/4582351/embed"))} 
        [:img {:src (str js/context "/img/music.png")}] "An Ode to Silence"]
    ]]])

(defonce music-url (r/atom ""))

(defn music-page []
  [:iframe#music {
    :width "100%"
    :height "100%"
    :frameBorder 0
    :allowFullScreen true
    :src @music-url}])

(def pages
  {:home #'home-page
  :experiments #'experiments-page
  :games #'games-page
  :compositions #'compositions-page
  :music #'music-page
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

(def nicescroll-config 
  #js {
    :cursorwidth "12px"
    :cursorcolor "rgb(46, 204, 113)"
    :autohidemode false
    :cursorborder "none"
    :background "rgb(221, 221, 221)"
    :cursorborderradius "0px"
  })

(defn nicescroll [id]
  (ready #(.niceScroll (js/jQuery id) nicescroll-config)))

(defn notebook-handler [data]
  (ready (fn [] 
    (reset! notebook-data
      (-> data js/JSON.parse js/nb.parse .render .-innerHTML))
    (js/setTimeout (fn []
      (js/Prism.highlightAll)
      (nicescroll "#app > .container")) 100))))

(secretary/defroute "/notebook/:id" [id]
  (reset! notebook-data nil)
  (session/put! :page :notebook)
  (GET (js/atob id) { :handler notebook-handler }))

(secretary/defroute "/compositions" []
  (session/put! :page :compositions))

(secretary/defroute "/music/:id" [id]
  (reset! music-url (js/atob id))
  (session/put! :page :music))

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
  (r/render [#'navbar] (js/document.getElementById "navbar"))
  (r/render [#'page] (js/document.getElementById "app")))

(defn init! []
  (load-interceptors!)
  (hook-browser-navigation!)
  (mount-components))
