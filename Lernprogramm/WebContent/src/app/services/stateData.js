let instance = null;
class stateData{
    constructor(){
        if(instance === null){
            instance = this;
        }
        return instance;
    }
    get currentChapter (){
        return this._chapter;
    }
    set currentChapter (chapter){
        this._chapter = chapter;
    }
    get currentLection (){
        return this._lection;
    }
    set currentLection (lection){
        this._lection = lection;
    }
}
export default stateData;
