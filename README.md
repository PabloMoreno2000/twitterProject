# Project 3 - *simpleTweet*

**simpleTweet** is an android app that allows a user to view his Twitter timeline and post a new tweet. The app utilizes [Twitter REST API](https://dev.twitter.com/rest/public).

Time spent: **20** hours spent in total

## User Stories

The following **required** functionality is completed:

* [1]	User can **sign in to Twitter** using OAuth login
* [2]	User can **view tweets from their home timeline**
  * [3] User is displayed the username, name, and body for each tweet
  * [4] User is displayed the [relative timestamp](https://gist.github.com/nesquena/f786232f5ef72f6e10a7) for each tweet "8m", "7h"
* [5] User can **compose and post a new tweet**
  * [6] User can click a “Compose” icon in the Action Bar on the top right
  * [7] User can then enter a new tweet and post this to twitter
  * [8] User is taken back to home timeline with **new tweet visible** in timeline
  * [9] Newly created tweet should be manually inserted into the timeline and not rely on a full refresh

The following **stretch** features are implemented:

* [10] User can **pull down to refresh tweets timeline**
* [11] User is using **"Twitter branded" colors and styles**
* [12] User sees an **indeterminate progress indicator** when any background or network task is happening
* [13] User can **see embedded image media within a tweet** on list or detail view.

The following **bonus** features are implemented:

* [14] User can view more tweets as they scroll with infinite pagination
* [15] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.org/android/Using-Parceler).


## Video Walkthrough

Here's a walkthrough of implemented user stories:

<img src='ST34.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).


## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Glide](https://github.com/bumptech/glide) - Image loading and caching library for Android

## License

    Copyright [2019] [Pablo Moreno]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
