//
//  ViewController.m
//  RCPoliceCar
//
//  Created by Andrei Vasilescu on 21/05/15.
//  Copyright (c) 2015 mready. All rights reserved.
//

#import "ViewController.h"
#import "MRStrengthEnum.h"

#define MIN_INDICATOR_DEGREES -15
#define MAX_INDICATOR_DEGREES 195
#define MIN_SPEED_PERCENT 20
#define MAX_SPEED_PERCENT 100

@interface ViewController ()

@property (weak, nonatomic) IBOutlet UIImageView *lightsImageView;
@property (weak, nonatomic) IBOutlet UIImageView *sirenLightsImageView;
@property (weak, nonatomic) IBOutlet UIImageView *signalStrengthImageView;

@property (weak, nonatomic) IBOutlet UIButton *leftSpeedButton;
@property (weak, nonatomic) IBOutlet UIButton *rightSpeedButton;

@property (weak, nonatomic) IBOutlet UIView *speedIndicatorView;

@end

@implementation ViewController {
    BOOL lightsActive;
    BOOL sirenActive;
    NSInteger degrees;
    NSInteger speedPercent;
    NSInteger defaultSpeedPercent;
    
    BOOL isGoingForward;
}

@synthesize lightsImageView;
@synthesize sirenLightsImageView;
@synthesize signalStrengthImageView;
@synthesize speedIndicatorView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    [self initDisplay];
    speedPercent = MIN_SPEED_PERCENT;
    defaultSpeedPercent = MIN_SPEED_PERCENT;
}

- (void)initDisplay {
    degrees = MIN_INDICATOR_DEGREES;
    [self updateSpeed];
}

- (void)setStrength:(MRStrength)strength {
    NSString *imageName = [MRStrengthEnum imageForStrength:strength];
    UIImage *image = [UIImage imageNamed:imageName];
    
    signalStrengthImageView.image = image;
}

- (void)updateSpeed {
    degrees = MIN_INDICATOR_DEGREES + (MAX_INDICATOR_DEGREES - MIN_INDICATOR_DEGREES) * speedPercent / 100;
    [UIView animateWithDuration:0.2 animations:^{
        speedIndicatorView.transform = CGAffineTransformMakeRotation(M_PI * degrees / 180.0f);
    }];
}

- (IBAction)onHornButtonTapped:(UIButton *)sender {
}

- (IBAction)onSirenButtonTapped:(UIButton *)sender {
    sirenActive = !sirenActive;
    sirenLightsImageView.highlighted = sirenActive;
}

- (IBAction)onLightsButtonTapped:(UIButton *)sender {
    lightsActive = !lightsActive;
    lightsImageView.highlighted = lightsActive;
}

- (IBAction)onLeftTouchDown:(UIButton *)sender {
}

- (IBAction)onLeftReleased:(UIButton *)sender {
}

- (IBAction)onRightTouchDown:(UIButton *)sender {
}

- (IBAction)onRightReleased:(UIButton *)sender {
}

- (IBAction)onUpTouchDown:(UIButton *)sender {
    isGoingForward = true;
    if (defaultSpeedPercent > 80) {
        speedPercent = 80;
        [self updateSpeed];
    }
    speedPercent = defaultSpeedPercent;
    [self updateSpeed];
}

- (IBAction)onLeftSpeedButtonTapped {
    if (isGoingForward) {
        if (speedPercent - 10 > MIN_SPEED_PERCENT) {
            speedPercent -= 10;
        } else {
            speedPercent = MIN_SPEED_PERCENT;
        }
    
        [self updateSpeed];
    }
}

- (IBAction)onRightButtonTapped {
    if (isGoingForward) {
        if (speedPercent + 10 < MAX_SPEED_PERCENT) {
            speedPercent += 10;
        } else {
            speedPercent = MAX_SPEED_PERCENT;
        }
    
        [self updateSpeed];
    }
}

- (IBAction)onUpReleased:(UIButton *)sender {
    isGoingForward = false;
    defaultSpeedPercent = speedPercent;
    speedPercent = 30;
    [self updateSpeed];
    speedPercent = 0;
    [self updateSpeed];
}

- (IBAction)onDownTouchDown:(UIButton *)sender {

}

- (IBAction)onDownReleased:(UIButton *)sender {
}


@end
