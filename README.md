# MyMarvelComics App 
Esse aplicativo foi feito para te ajudar a salvar seus quadrinhos favoritos! :)

## Arquitetura
**MyMarvelComicsApp** é baseado na arquitetura MVVM e o Repository pattern, seguindo [Guia de arquitetura docuemntada pelo google](https://developer.android.com/topic/)
Além disso, também incluo Clean Architecture.

<img width="960" alt="Canva Design" src="https://github.com/user-attachments/assets/509d5dff-d49c-4dad-ae70-e73026dab3f3">


# SYTEM DESIGN 

  - ## Paginação:

  <img src="https://developer.android.com/static/topic/libraries/architecture/images/paging3-layered-architecture.svg?hl=pt-br">

Vale notar que na imagem acima não está fazendo alusão a uma camada de UseCases a qual estou usando nesse projeto. Faço o uso de use cases justamente para encapsular as regras de negcócio da aplicação.
Além disso, não faço o uso do PagingDataAdapter, o qual é usado em RecyclerViews. Apenas recebo da ViewModel um PagingData e faço o consumo utilizando uma @Composable

## 1. Objetivo do CharactersRemoteMediator

O CharactersRemoteMediator é uma classe que faz parte da implementação de paginação de dados usando o componente Paging 3 do Android Jetpack. O propósito dessa classe é gerenciar a carga e a atualização dos dados do model vindo retornados pelo Web Server da Marvel, e armazená-los localmente no nosso AppDatabase. Ela lida com a lógica de paginação e cache para fornecer uma experiência de rolagem suave e eficiente para o usuário.

## 2. Arquitetura e Componentes

Componentes Principais:
RemoteMediator: Interface fornecida pelo Paging do android para lidar com a intermediação entre a origem de dados remota e o cache local.
charactersDao: DAO (Data Access Object) para acessar e manipular o banco de dados local.
remoteService: Serviço para fazer requisições à API remota.
transactionProvider: Fornece uma maneira de executar transações no banco de dados, garantindo a integridade dos dados.

## 3. Fluxo de Dados

Inicialização (initialize)

Quando o RemoteMediator é inicializado, ele retorna a ação LAUNCH_INITIAL_REFRESH, indicando que uma atualização inicial dos dados deve ser realizada.
Carregamento de Dados (load)

LoadType: A função load é chamada com um tipo de carregamento que pode ser REFRESH, PREPEND, ou APPEND.

LoadType.REFRESH: Limpa o cache local e carrega dados novos. Define currentPage como o valor inicial.
LoadType.PREPEND: Não faz nada e retorna endOfPaginationReached = true, indicando que não há mais dados a serem carregados antes do início.
LoadType.APPEND: Adiciona novos dados à lista existente, incrementando a currentPage e ajustando a chave de carregamento.
Requisição à API:

Se comicId for fornecido, faz uma requisição específica para obter personagens de uma HQ específica. (esse caso, uso para reutilizar esse Remoto Mediator para paginação uma lista de personagens, por exemplo, por meio de um comicId. O mesmo faço o ComicsRemoteMediator). Caso contrário, faz uma requisição geral para obter todos os personagens.

## Transformação e Armazenamento:

Converte os dados da resposta da API em entidades CharacterEntity.
Usa o transactionProvider para executar uma transação que, dependendo do LoadType, limpa o banco de dados local e insere os novos dados.

## Resultado da Mediação:

Retorna MediatorResult.Success se os dados foram carregados com sucesso, indicando se todos os dados foram carregados (endOfPaginationReached).
Retorna MediatorResult.Error em caso de falhas (IOException, HttpException, ou outros erros).

## 4. Benefícios e Justificativas:

- ### Paginação Eficiente: Utiliza a abordagem de paginação para carregar dados de forma incremental, o que melhora o desempenho e a experiência do usuário.
- ### Atualização e Sincronização: Garante que o banco de dados local esteja sincronizado com os dados mais recentes da API.
- ### Tratamento de Erros: Lida com diferentes tipos de erros de forma robusta, fornecendo feedback apropriado.

## 5. Em resumo:

O CharactersRemoteMediator é uma solução eficiente para gerenciar dados paginados em um aplicativo Android, proporcionando uma maneira limpa e robusta de integrar dados remotos com o cache local. A implementação segue as melhores práticas para garantir uma experiência de usuário responsiva e um código limpo e manutenível.

  - ## Favoritos

    ### O sistema de favoritação foi pensado para que os usuários possam armazenar os HQs em disco local.

    - Na imagem abaixo, apresentamos uma visão geral do design do MyComicsApp. Anteriormente, ao discutir o sistema de paginação, mencionamos o DAO (Data Access Object). O DAO é uma interface responsável por abstrair as implementações de consultas em SQL, facilitando o acesso e a manipulação dos dados.
   
                                                    UI LAYER      DOMAIN LAYER          DATA LAYER            
             Em resumo: INTERAÇÃO DO USUÁRIO -> VIEW -> VIEWMODEL -> USECASE -> REPOSITORY -> DAO -> DATABASE  

<img src="https://developer.android.com/static/codelabs/basic-android-kotlin-compose-persisting-data-room/img/8b91b8bbd7256a63_1920.png?hl=pt-br">

<body>
    <h1>Estrutura do projeto</h1>
    <pre>
<code>
├── Core
│   ├── Domain
│   ├── Data (repositories)
│   ├── Database (DAO, model/entities, converters)
│   ├── UI (commons, components)
|   ├── Models
│   ├── Network
├── Features
│   ├── Characters (contendo as screens e estrutura de navegação)
│   ├── Comics (contendo as screens e estrutura de navegação)
</code>
    </pre>
</body>

# CORE
  - ### Contém os componentes centrais que são utilizados em todo o aplicativo.
  
  ------------
# Domain
Contém as classes de domínio responsáveis para que lidar com as regras de negócio do aplicativo. 
O ideal é deixar esse componente o mais isolado possível de frameworks externos. Desse modo, fica mais fácil de testar e escalar.
- ## Exemplo: 
A AddOrRemoveFromFavoritesImpl é responsável por verificar se o Comic marcado é favorito ou não para a partir disso decidir se vai adicionar ou deletar os item na table FAVORITES.
Nesse exemplo, nós podemos identificar 2 patterns importantes em desenvolvimento de sistemas: princípio da inversão de dependência e command pattern.
     
```
class AddOrRemoveFromFavoritesImpl(
    private val favoriteComics: ComicsRepository,
) : AddOrRemoveFromFavorites {
    override suspend fun invoke(
        comicId: Int,
        model: Comic,
        onResult: (ResultOf<Int>) -> Unit,
    ) {
        if (model.isFavorite) {
            favoriteComics.deleteFavoriteComic(comicId, onResult)
        } else {
            favoriteComics.addFavoriteComic(model, onResult)
        }
    }
}
```

# O que seria um Command Pattern?
Padrão de design comportamental que encapsula uma solicitação como um objeto, permitindo que você parametrize métodos com diferentes solicitações, enfileire ou registre solicitações e suporte operações que podem ser desfeitas..
Nesse contexto, a interface AddOrRemoveFromFavorites define a operação de adicionar/remover favoritos. A implementação decide a ação com base no estado do `comic` e chama métodos no ComicsRepository (o Receiver). Já o client, nosso ComicDetailsViewModel pode atuar usando o Invoker que dispara a execução da operação.

- ## Benefícios: Permite adicionar novas operações sem modificar o código existente.
    
- ## COMO ASSIM?

 ### Vamos para outro contexto.
  - Anterioramente, O ComicsUseCase estava responsável apenas para chamar o fluxo de dados da paginação vindo da camada de dados. Os dados da paginação vêm puros do servidor (sem saber se é um comic favoritado ou não). Caso queiramos retornar uma lista de favoritos para mostrar itens misturados para o usuário (e com algum componente de visualizão, claro), basta nós acrescentarmos mais lógica dentro da implementação. Resultado: o retorno do contrato não muda ainda, apenas a implementação. Isso é muito flexível e fácil de escalar.
    
    - ## OK, "Cadê o código?"
 
 ```
class ComicsUseCaseImpl (private val comicsRepository: ComicsRepository):
    ComicsUseCase {
    override fun invoke(): Flow<PagingData<Comic>> {

       // this.client pede 15 itens por vez

        return comicsRepository.getPagingComics(15).flow.map {

         // fluxo de paginação

            it.map { comicEntity ->
                withContext(Dispatchers.IO) {
                    val call: suspend () -> Comic = {
                        async {
                            var resultComic: Comic? = null

                           // verifica com o repository se esse item é favorito ou não

                            comicsRepository.readFavoriteComic(comicEntity.id) {

                               // se for, retorna como favorito, caso contrário, segue em frente

                                resultComic = when (it) {
                                    is ResultOf.Success -> {
                                        it.value
                                    }
                                    is ResultOf.Failure -> {
                                        comicEntity.toComic()
                                    }
                                }
                            }
                            resultComic!!

                      // esperando pelo resultado: muito comum e útil para operações assíncronas

                        }.await()
                    }
                    call()
                }
            }
        }
    }
}
```

Ao longo do codebase você vai perceber o quanto fica fácil seguir esse design. Até mesmo para, caso ncessário, componentizar essas classes utilizando generics do kotlin.

# SOBRE COMPONENTIZAÇÃO
É muito comum ao longo do densenvolvimento de um software haver a necessidade de reutilizar abstrações.
Nesse projeto tem uma que gostei bastante, pois a criei pensando justamente para reutilizar na criação de telas semelhantes, uma prática muito comum para não somente ecomizarmos tempo, mas para tertarmos.
No arquito PagingComponents.kt você vai encontrar alguns exemplos de componentes que criei para reutilizar, como cards e listas.
E esse é meu favorito :
```        
@Composable
internal fun <T : Any> LazyVerticalGridPaging(
    modifier: Modifier,
    items: LazyPagingItems<T>,
    content: @Composable ColumnScope.(T?) -> Unit,
) {
    Box(modifier = modifier) {
        if (items.loadState.refresh is LoadState.Loading) {
            LoadingWithDeadPool(modifier = Modifier.align(Alignment.Center))
        } else {
            LazyVerticalGrid(
                contentPadding = PaddingValues(8.dp),
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .padding(top = 16.dp)
                    .fillMaxSize()
            ) {
                items(count = items.itemCount) { index ->
                    val item = items[index] as T?
                    CardScaffold(
                        Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .aspectRatio(0.8f)) {
                        content(item)
                    }
                }
            }
        }
        if (items.loadState.append is LoadState.Loading) {
            LoadingMoreItemsIndicator(Modifier.align(Alignment.BottomCenter))
        }
    }
}
```
Com o recurso de Generics do kotlin fica fácil de usar para qualquer tipo de dado, e é assim que faço para renderizar tanto a página de Comics quanto a de Characters

  ## Como utilizar? 
     
    // aqui basta chamar a composable. No escopo que ela retorna você pode incluir qualquer tipo de composable. Você também pode usar o componente de Card criado e passar os parâmetros que nele contém
    // no contexto pra essa aplicação, os principais forão 
      - isFavorite
      - titulo
      - url da image 
     LazyVerticalGridPaging { item ->
         item.apply {
           CardContent(isFavorite, title, url)
         }
     }

## Data: Repositórios responsáveis por buscar e armazenar dados.
## Database: Data Access Objects (DAOs) e entidades do banco de dados.
## UI: Componentes de interface do usuário comuns.
## Network: Configurações e chamadas de rede.
## Features: Contém as funcionalidades específicas do aplicativo.
## Characters: Contém as telas e a estrutura de navegação relacionadas aos personagens.
## Comics: Contém as telas e a estrutura de navegação relacionadas às HQs.
## O dagger-hilt é um facilitador para conteinização da applicação, ficando responsável por injetar e inicializar nossos componentes conforme nossos critérios.

# LINKS DE CONTEÚDOS RELACIONADOS:
 [Banco de dados local no android](https://developer.android.com/training/data-storage/room?hl=pt-br)
 [RemoteMediators](https://developer.android.com/topic/libraries/architecture/paging/v3-network-db?hl=pt-br)

 ## Screenshots📱

| Listagem de HQ's                             | Detalhes de HQ                            | Carregando mais HQs               |
|----------------------------------------------|-------------------------------------------|-------------------------------------|
| <img src="https://github.com/user-attachments/assets/f13b8fd2-5f8b-4095-9091-956cdb858f95" alt="Alt text" style="width: 188px; height: 412px;"> | <img src="https://github.com/user-attachments/assets/66918b67-5193-4447-b572-2fe0b4c86235" alt="Alt text" style="width: 188px; height: 412px;">| <img src="https://github.com/user-attachments/assets/2335f483-3b6c-46fe-b91a-602b39949547" alt="Alt text" style="width: 188px; height: 412px;"> |

| Favoritando HQ                               | Excluindo HQ dos favoritos                | Sessão de favoritos               |
|----------------------------------------------|-------------------------------------------|-----------------------------------|
| <img src="https://github.com/user-attachments/assets/fa36f496-2a5b-4f19-ac35-95edfd1d3b9e" alt="Alt text" style="width: 188px; height: 412px;"> | <img src="https://github.com/user-attachments/assets/c0a85dfa-66cb-4845-9114-17a3c24d32a5" alt="Alt text" style="width: 188px; height: 412px;">| <img src="https://github.com/user-attachments/assets/c6ea4cdf-90c8-425e-987d-6937675f3fbe" alt="Alt text" style="width: 188px; height: 412px;"> |

| App Modal Drawer                             | Lista de Personagens                      | Detalhes do personagem              |
|----------------------------------------------|-------------------------------------------|-----------------------------------|
| <img src="https://github.com/user-attachments/assets/43cfc35c-79d3-4891-a998-90937f55b9c7" alt="Alt text" style="width: 188px; height: 412px;"> | <img src="https://github.com/user-attachments/assets/d4070244-1ec4-4146-a290-7f74a94c9e79" alt="Alt text" style="width: 188px; height: 412px;">| <img src="https://github.com/user-attachments/assets/ebcb83dc-c113-457e-aae5-ed1d5ab7f46b" alt="Alt text" style="width: 188px; height: 412px;">|


## Funcionalidades 🔬

-  Listagem de todos os personagens da marvel.
> 
-  Listagem de todos os HQ's da marvel.
> 
-  Salvar ou excluir HQ's como favorito.
> 
-  Listagem de HQ's por personagem selecionado.
>
-  Listagem de Personagens por HQ selecionado.

## :hammer: O que precisa para dar build na applicação?

- [Registre uma chave](https://developer.marvel.com/)
- Você deve adicionar `PUBLIC_API_KEY` e a `PRIVATE_API_KEY` em seu `local.properties` para dar build na app, assim:

    ```kotlin
    PUBLIC_API_KEY=PUBLIC_API_KEY
    PRIVATE_API_KEY=PRIVATE_API_KEY
    ```

## Summary Technologies 🛠️
This Project is created with:
* *Interceptors*
* *Robolectric*
* *MockWebserver*
* *Unit Tests*
* *Room*
* *Compose*
* *Dagger - Hilt*
* *SavedStateHandle*
* *Flow*
* *Coroutines*
* *Navigation*
* *ViewModel*
* *Material Design*
* *Retrofit*
* *Coil*
* *Kotlin*
* *OkhttpClient*
* *Pagging*
