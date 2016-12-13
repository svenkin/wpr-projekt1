    export default {
        getChapters :  () => {
                    return new Promise((resolve,reject)=>{
                        fetch('https://jsonplaceholder.typicode.com/posts/1').then((response)=>{
                            return response.json();
                            }).then((data)=>{
                            console.info(data);
                                resolve(data);
                            });
                        })
                    }
    }
