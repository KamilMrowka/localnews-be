package com.kamil.dev.local.news.demo.processors;

import com.kamil.dev.local.news.demo.dao.dto.AssigningCityDTO;
import com.kamil.dev.local.news.demo.dao.entities.ArticleEntity;
import com.kamil.dev.local.news.demo.dao.entities.CityEntity;
import com.kamil.dev.local.news.demo.dao.repositories.ArticleRepository;
import com.kamil.dev.local.news.demo.dao.repositories.CityRepository;
import com.kamil.dev.local.news.demo.parsers.GptResponseParser;
import com.kamil.dev.local.news.demo.services.ArticleService;
import com.kamil.dev.local.news.demo.services.OpenAiService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class CityAssigner {
    private final CityRepository cityRepository;
    private final ArticleRepository articleRepository;
    private final GptResponseParser gptResponseParser;
    private final OpenAiService openAiService;

    public void migrateArticles(boolean onlyNew) {
        List<ArticleEntity> articles;

        if (onlyNew) {
            articles = articleRepository.findArticleEntitiesByCityIdAndGlobal(null, false);
        } else {
            articles = articleRepository.findAll();
        }

        List<CompletableFuture<String>> futures = new ArrayList<>();
        int batchSize = 15;

        // Send them in batches of size = batchSize to make sure it's reliable
        for (int i = 0; i < articles.size(); i += batchSize) {
            List<ArticleEntity> nextArticles = articles.subList(i, Math.min((i + batchSize), articles.size()));
            String prompt = generateAssigningPrompt(nextArticles);
            CompletableFuture<String> future = openAiService.getChatCompletions(prompt);
            futures.add(future);
        }

        CompletableFuture.allOf(futures.toArray(new CompletableFuture[0])).join();

        List<String> responses = new ArrayList<>();

        for (CompletableFuture<String> future : futures) {
            try{
                responses.add(future.get());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        for(String response : responses) {
            executeResponse(response);
        }
    }

    public void migrateArticles() {
       migrateArticles(false);
    }

    private String generateAssigningPrompt(List<ArticleEntity> articles) {

        String formattedArticles = formatArticlesForPrompt(articles);

        String prompt = String.format(
        """
        You are an experienced news director working for a usa newspaper. I will give you a list of articles in a specified format:
        [{title, article_beginning},{another_title, another_article_beginning}]
        Your job is to decide whether an article is global or local. Local means a specific city in the us, decide what city and state it belongs to.
        Some important factors to look at when deciding:
        - article is about something outside of usa -> it's global
        - about a celebrity or a household name -> it's global unless the context suggests otherwise
        - article is about something only relevant to specific area(if you live outside of the city it doesn't directly affect you) -> local
        - article is about something that is relevant to everyone in the us OR in the whole world, like some general knowledge -> global
        - scale of the mentioned situation would be interesting to people from around the usa or around the world(like multiple people murdered, huge money mentioned, huge companies) -> global
        - if you cannot easily decide what city the article belongs to -> likely global
        - if there's a city mentioned it doesn't necessarily mean it's local(think about the scale, relevancy outside of the city)
        
        Additionaly:
        - use full city names as well as state names. For example: use 'New Jersey' and NOT 'NJ'
        - Make sure you checked ALL articles and included them in your response
        - if article is global but there's some particular easy to deduct us city it's most related to you can set its city_name and state_name properties
        
        Example input:
        [
            {
                id: 0,
                WATCH: Laura Curran Has No Plans To Challenge Blakeman Next Year,
                BALDWIN, NY — With Election Day within three weeks, former Nassau County Executive Laura Curran looks at some of the biggest races Long Islanders will find at the ballot boxes. \\\\n\\\\nRep. Anthony D'Esposito can expect a nailbiter in a rematch with Democrat Laura Gillen, the former Town of Hempstead Supervisor, for the 3rd Congressional District.\\\\n\\\\n\\\\"This is a very important seat t"
            },
            {
                id: 1,
                $5.5M for Jasper tourism recovery pledged by federal, provincial governments,
                The Alberta and federal governments are putting a combined $5.5 million toward attracting tourists to the Rocky Mountain town of Jasper .\\n\\nA July wildfire destroyed one-third of the town, including multiple hotels and 800 housing units.\\n\\nYear-round tourism is Jasper’s main economic driver as close to 2.5 million people visited the surrounding national park last year alone.\\n\\nSoraya Martinez Ferrada, the federal tourism minister, said Friday that Ottawa will spend $3 million on tourism advertising across the globe to attract visitors back to Jasper.\\n\\n“This summer’s wildfires across Western Canada were devastating to communities like Jasper that depend on tourism to drive their economy,” Ferrada said in a news release.\\n\\n“I know that through concerted efforts with partners like Destination Canada and Travel Alberta, the municipality and region will bounce back, ready to once again host world-calibre events and welcome visitors from near and far.”\\n\\nJoseph Schow, Ferrada’s Alberta counterpart, said Friday that his province is putting $2.5 million toward the same goal.\\n\\n“This funding will help Jasper’s tourism businesses prepare to welcome visitors this fall and winter, ensuring they remain viable and ready for next summer,” he said in a news release.\\n\\nSchow also said that some of the provincial funding will be earmarked for tourism businesses in Jasper to help rebuild and create new visitor experiences.\\n\\n“Tourism is the backbone of Jasper’s economy, and for the town to rebuild, we need to see a strong return of tourism businesses, accommodation providers, services and experiences,” he said in the release.\\n\\nThe Insurance Bureau of Canada estimates the July fire caused at least $880 million in insured damages, which is the costliest event in national park history.\\n\\nThe $880 million total is thought to be the ninth highest natural disaster
            },
            {
                id: 2,
                Middletown BOE Member: School Boards Should Not Vote On HIB Reports,
                MIDDLETOWN, NJ — One of the questions asked \\n\\n was how to reduce bullying in the district.\\n\\nIt was the first question asked, at minute 12:36, and was as follows: \\"What steps would you put in place to ensure there is a safe and accepting learning environment for all children, free from bullying, intimidation, harassment and violence?\\"\\n\\nLongtime board member Joan Minnuies, who is seeking re-election, had a unique answer: She said in her opinion, school board should not be voting on HIB (harassment, intimidation and bullying) complaints.\\n\\n\\"I really truthfully don't believe that a Board of Education should have to vote on bullying,\\" said Minnuies, as part of her answer. \\"We are not part of the evaluation, the interviews that go on. Once in a while, we get a clear view of an issue that's bullying. The rest of the time, we're not there, how do we really know?\\"\\n\\nIt's a novel idea. But Minnuies' opinion is not likely to generate much change.\\n\\nIn New Jersey, school boards are required to review HIB complaints, and issue a final decision on the superintendent's assessment of the complaint's validity. Parents can then appeal that decision to the BOE if they disagree.\\n\\nFrom the NJ School Boards Association:\\n\\n\\"The superintendent is required to report the results of each HIB investigation to the board of education at a board meeting and, at the next board meeting, the board of education is required to issue a decision, in writing, to affirm, reject or modify the superintendent’s decision.\\"\\n\\nMinnuies did not respond when Patch asked her to elaborate on her comments.\\n\\n \\n\\n (July 2024)\\n\\n (June 2024)\\n\\n
            },
        ]
        
        Output for the example(respond ONLY with the following json format):
        {
            "articles": [
                {
                    "id": 0,
                    "global": false,
                    "city_name": "Baldwin",
                    "state_name": "New York"
                },
                {
                    "id": 1,
                    "global": true,
                    "city_name": "000",
                    "state_name": "000"
                },
                {
                    "id": 2,
                    "global": false,
                    "city_name": "Middletown",
                    "state_name": "New Jersey"
                },
            ]
        }
        
        My explaination (NOT PART OF THE OUTPUT, IT'S JUST FOR SHOWING THE REASONING BEHIND IT):
            1st. Article about small elections. Not relevant to people from outside the region.
            2nd. Article mentions big sums of money(interesting to people from outside the area) + it's scope is outside us.
            3rd. Article about a certain school and a situation with a small range of interest.
            
        Actual input:
        %s
        
        Output:
        """, formattedArticles);

        return prompt;
    }

    private String formatArticlesForPrompt(List<ArticleEntity> articles) {
       String formatted = "";

       for (ArticleEntity article : articles) {
           String addidion = "{ \"id\": " + article.getId() + ",\"" + article.getTitle() + "\"" + "," + "\"" + article.getArticle() + "\"" + "}, ";
           formatted = formatted.concat(addidion);
       }

       return formatted;
    }

    private void executeResponse(String response) {
        List<AssigningCityDTO> parsedDTOS = gptResponseParser.parseGptResponse(response);

        if (parsedDTOS != null) {
            assignCities(parsedDTOS);
        }
    }

    private void assignCities(List<AssigningCityDTO> dtos) {
        for(AssigningCityDTO dto : dtos) {
            Optional<ArticleEntity> article = articleRepository.findArticleEntityById(dto.getId());
            Optional<CityEntity> correspondingCity = cityRepository.findCityEntityByStateNameAndName(dto.getStateName(), dto.getCityName());

            if(article.isPresent()) {
                ArticleEntity assigning = article.get();
                // If global no need to save city and state
                // Also there is no valuable city and state then
                if (dto.isGlobal()) {
                    assigning.setGlobal(true);
                    articleRepository.save(assigning);
                } else {
                   if(correspondingCity.isPresent()) {
                      CityEntity city = correspondingCity.get();
                       articleRepository.save(assigning);
                      assigning.setCityId(city.getId());
                      articleRepository.save(assigning);
                   }
                }
            }
        }
    }
}
