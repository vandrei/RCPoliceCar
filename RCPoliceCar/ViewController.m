//
//  ViewController.m
//  RCPoliceCar
//
//  Created by Andrei Vasilescu on 21/05/15.
//  Copyright (c) 2015 mready. All rights reserved.
//

#import "ViewController.h"
#import "MRStrengthEnum.h"

#define INITIAL_INDICATOR_DEGREES -15

@interface ViewController ()

@property (weak, nonatomic) IBOutlet UIImageView *lightsImageView;
@property (weak, nonatomic) IBOutlet UIImageView *sirenLightsImageView;
@property (weak, nonatomic) IBOutlet UIImageView *signalStrengthImageView;

@property (weak, nonatomic) IBOutlet UIView *speedIndicatorView;

@end

@implementation ViewController {
    BOOL lightsActive;
    BOOL sirenActive;
    NSInteger degrees;
}

@synthesize lightsImageView;
@synthesize sirenLightsImageView;
@synthesize signalStrengthImageView;
@synthesize speedIndicatorView;

- (void)viewDidLoad {
    [super viewDidLoad];
    // Do any additional setup after loading the view, typically from a nib.
    [self initDisplay];
}

- (void)initDisplay {
    degrees = INITIAL_INDICATOR_DEGREES;
    [self updateSpeed:degrees];
}

- (void)setStrength:(MRStrength)strength {
    NSString *imageName = [MRStrengthEnum imageForStrength:strength];
    UIImage *image = [UIImage imageNamed:imageName];
    
    signalStrengthImageView.image = image;
}

- (void)updateSpeed:(NSInteger)degrees {
    [UIView animateWithDuration:0.2 animations:^{
        speedIndicatorView.transform = CGAffineTransformMakeRotation(M_PI * degrees / 180.0f);
    }];
}

- (IBAction)onHornButtonTapped:(UIButton *)sender {
    degrees = INITIAL_INDICATOR_DEGREES;
    [self updateSpeed:degrees];
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
    degrees += 5;
    [self updateSpeed:degrees];
}

- (IBAction)onUpReleased:(UIButton *)sender {
}

- (IBAction)onDownTouchDown:(UIButton *)sender {
    degrees -= 5;
    [self updateSpeed:degrees];
}

- (IBAction)onDownReleased:(UIButton *)sender {
}



@end
