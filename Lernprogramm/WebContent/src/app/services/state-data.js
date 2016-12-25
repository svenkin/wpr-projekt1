export default class StateData {
    static get instance() {
        if (!StateData._instance) StateData._instance = new StateData();
        return StateData._instance;
    }
}
