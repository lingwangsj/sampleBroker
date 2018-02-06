/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, async } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs/Observable';

import { SampleBrokerTestModule } from '../../../test.module';
import { APGroupMySuffixDetailComponent } from '../../../../../../main/webapp/app/entities/ap-group-my-suffix/ap-group-my-suffix-detail.component';
import { APGroupMySuffixService } from '../../../../../../main/webapp/app/entities/ap-group-my-suffix/ap-group-my-suffix.service';
import { APGroupMySuffix } from '../../../../../../main/webapp/app/entities/ap-group-my-suffix/ap-group-my-suffix.model';

describe('Component Tests', () => {

    describe('APGroupMySuffix Management Detail Component', () => {
        let comp: APGroupMySuffixDetailComponent;
        let fixture: ComponentFixture<APGroupMySuffixDetailComponent>;
        let service: APGroupMySuffixService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [SampleBrokerTestModule],
                declarations: [APGroupMySuffixDetailComponent],
                providers: [
                    APGroupMySuffixService
                ]
            })
            .overrideTemplate(APGroupMySuffixDetailComponent, '')
            .compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(APGroupMySuffixDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(APGroupMySuffixService);
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                spyOn(service, 'find').and.returnValue(Observable.of(new HttpResponse({
                    body: new APGroupMySuffix(123)
                })));

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(service.find).toHaveBeenCalledWith(123);
                expect(comp.aPGroup).toEqual(jasmine.objectContaining({id: 123}));
            });
        });
    });

});
