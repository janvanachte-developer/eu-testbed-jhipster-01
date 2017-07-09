/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { JhiDateUtils, JhiDataUtils, JhiEventManager } from 'ng-jhipster';
import { TestbedTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { TestStepDetailComponent } from '../../../../../../main/webapp/app/entities/test-step/test-step-detail.component';
import { TestStepService } from '../../../../../../main/webapp/app/entities/test-step/test-step.service';
import { TestStep } from '../../../../../../main/webapp/app/entities/test-step/test-step.model';

describe('Component Tests', () => {

    describe('TestStep Management Detail Component', () => {
        let comp: TestStepDetailComponent;
        let fixture: ComponentFixture<TestStepDetailComponent>;
        let service: TestStepService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [TestbedTestModule],
                declarations: [TestStepDetailComponent],
                providers: [
                    JhiDateUtils,
                    JhiDataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    TestStepService,
                    JhiEventManager
                ]
            }).overrideTemplate(TestStepDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(TestStepDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(TestStepService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new TestStep(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.testStep).toEqual(jasmine.objectContaining({id: 10}));
            });
        });
    });

});
