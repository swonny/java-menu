package controller;

import menu.Coach;
import repository.CoachRepository;
import repository.MenuForWeekRepository;
import view.InputView;
import view.OutputView;

import java.util.List;
import java.util.stream.Collectors;

public class MenuController {
    private static final int MINIMUM_COACHES = 2;
    private final OutputView outputView;
    private final InputView inputView;

    public MenuController(OutputView outputView) {
        this.outputView = outputView;
        this.inputView = new InputView(outputView);
    }

    public void run() {
        outputView.printStartMessage();
        List<Coach> coaches = makeCoaches(inputView.readCoachNames());
        for (Coach coach : coaches) {
            List<String> foodNames = getHateFoodNames(inputView.readNotAvailableFood(coach.getName()), coach.getName());
            coach.addHateFoodList(foodNames);
        }
        outputView.printMenuResult(MenuForWeekRepository.getMenuResult());
    }

    private List<String> getHateFoodNames(String hateFoodNames, String coachName) {
        try {
            List<String> foodNames = splitNames(hateFoodNames);
            validateFoodSize(foodNames);
            validateFoodNames(foodNames);
            return foodNames;
        } catch (IllegalArgumentException exception) {
            outputView.printExceptionMessage(exception);
            return getHateFoodNames(inputView.readNotAvailableFood(coachName), coachName);
        }
    }

    private void validateFoodNames(List<String> foodNames) {
        // TODO : 정확한 메뉴 이름인지 검사 추가
    }

    private void validateFoodSize(List<String> splitFoodNames) {
        // TODO : 하드코딩 상수화
        if (splitFoodNames.size() > 2) {
            throw new IllegalArgumentException("메뉴는 최대 두 개 이상 입력할 수 없습니다.");
        }
    }

    private List<Coach> makeCoaches(String coachNames) {
        try {
            List<String> names = splitNames(coachNames);
            validateCoachSize(names);
            List<Coach> coaches = makeCoaches(names);
            CoachRepository.initializeCoaches(coaches);
            return coaches;
        } catch (IllegalArgumentException exception) {
            outputView.printExceptionMessage(exception);
            return makeCoaches(inputView.readCoachNames());
        }
    }

    private void validateCoachSize(List<String> names) {
        if (names.size() < MINIMUM_COACHES) {
            throw new IllegalArgumentException("코치는 최소 2명 이상 입력해야 합니다.");
        }
    }

    private List<Coach> makeCoaches(List<String> coachNames) {
        return coachNames.stream().map(name -> new Coach(name))
                .collect(Collectors.toList());
    }

    private List<String> splitNames(String coachNames) {
        return List.of(coachNames.split(","));
    }
}
